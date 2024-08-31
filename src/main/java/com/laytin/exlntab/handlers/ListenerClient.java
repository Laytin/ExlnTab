package com.laytin.exlntab.handlers;

import com.laytin.exlntab.proxy.ResourcesProxy;
import com.laytin.exlntab.render.DrawUtil;
import com.laytin.exlntab.render.PhotoRender;
import com.laytin.exlntab.render.font.FontStyles;
import com.laytin.exlntab.utils.PlayerInfoObj;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@SideOnly(Side.CLIENT)
public class ListenerClient {
    public Map<String, PlayerInfoObj> playerList = new HashMap<>();
    public static String tps = "20";
    private final Minecraft mc = Minecraft.getMinecraft();
    private float referenceWidth = 427.0F;
    private float referenceHeight = 240.0F;
    private float scaleX = 1.0F;
    private float scaleY = 1.0F;
    private float minScale = 1.0F;
    private float alpha =0.0f;
    private float scrollOffset =0;
    private float maxScrollOffset =0;
    private static ListenerClient instance;
    public static ListenerClient getInstance() {
        return instance;
    }
    public ListenerClient() {
        instance=this;
    }

    @SubscribeEvent(
            priority = EventPriority.HIGH
    )
    public void eventHandlerTabRender(RenderGameOverlayEvent.Pre event) {
        if(!Keyboard.isKeyDown(Keyboard.KEY_TAB) && alpha!=0.0f){
            alpha=0.0f;
            scrollOffset=0;
        }

        if (event.type == RenderGameOverlayEvent.ElementType.PLAYER_LIST)
            event.setCanceled(true);

        ScoreObjective scoreobjective = this.mc.theWorld.getScoreboard().func_96539_a(0);
        NetHandlerPlayClient handler = this.mc.thePlayer.sendQueue;
        int widthScreen = event.resolution.getScaledWidth();
        int heightScreen = event.resolution.getScaledHeight();
        if (this.mc.gameSettings.keyBindPlayerList.getIsKeyPressed() && (!this.mc.isIntegratedServerRunning()
            || handler.playerInfoList.size() > 1 || scoreobjective != null)
            && event.type == RenderGameOverlayEvent.ElementType.TEXT) {
            recalculateScale(widthScreen,heightScreen);
            ArrayList<GuiPlayerInfo> players = new ArrayList(handler.playerInfoList);
            List<String> playerNames = players.stream().map(f->f.name).collect(Collectors.toList());
            maxScrollOffset = Math.max(0,(playerNames.size()/4)*20 - 125F);
            drawBackground(widthScreen,heightScreen,
                    players.stream().filter(f->f.name.equals(this.mc.getSession().getUsername())).findFirst().get().responseTime, players.size());
            drawScissorList(widthScreen,heightScreen,playerNames);
            drawScrollRect(widthScreen,heightScreen);

            if(alpha<0.9)
                alpha+=0.1f;
        }
    }
    private void drawScrollRect(int widthScreen, int heightScreen){
        if(maxScrollOffset==0)
            return;
        float ww =(125/ (125+maxScrollOffset));
        DrawUtil.drawRectFloat(widthScreen/2+145*minScale , heightScreen/2 -54*minScale+ scrollOffset*minScale*ww,
                widthScreen/2+146*minScale, Math.min(heightScreen/2 -50*minScale + ww*minScale*110 + scrollOffset*minScale*ww, heightScreen/2 - 55*minScale + minScale * 125F), Color.white.getRGB(), alpha);
    }
    private void drawScissorList(int widthScreen, int heightScreen,List<String> list1){
        //pushmatrix to move it to float coords cuz scissor can be called only with int values
        GL11.glPushMatrix();
        GL11.glTranslatef(widthScreen/2-140*minScale, heightScreen/2 - 55*minScale, 0.0F);
        //glscissor
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        glScissor((widthScreen/2-140*minScale), (heightScreen/2 - 55*minScale),(minScale * 280F),  (minScale * 125F));
        //main render
        AtomicInteger a = new AtomicInteger();
        mc.thePlayer.addChatMessage(new ChatComponentText("size:"+maxScrollOffset));
        for(PlayerInfoObj ob : playerList.values()) {
            if (!list1.contains(ob.getUsername())) //player in vanish
                continue;
            float xVal = ((a.get()) % 4) * 71.3F * minScale;
            float yVal = ((a.get()) / 4) * 20 * minScale - scrollOffset * minScale;
            DrawUtil.drawImage(xVal, yVal, 66 * minScale, 15 * minScale, ResourcesProxy.buttonHover, alpha);
            //Avatar
            PhotoRender.getInstance().drawAvatar(ob.getUsername(), xVal + 2 * minScale, yVal + 2 * minScale, 11 * minScale, 11 * minScale, 1);
            //Name
            DrawUtil.drawStringWithDepth(FontStyles.SemiBold.getFontContainer(), FontStyles.SemiBold.getFontContainer().getTextFont().trimStringToWidth(ob.getUsername(), 200, false),
                    xVal + 15 * minScale, yVal + 4 * minScale, 0.2F * minScale, Color.WHITE.getRGB());
            //roles
            if (ConfigHandler.drawRoles) {
                String drawRoleText = ConfigHandler.useGroupName ? ob.getRole() : ob.getRoleDisplayName();
                drawRoleText = drawRoleText.equals("exlntab") ? "Player" : drawRoleText;
                if (!ConfigHandler.useColorCodes) {
                    while (drawRoleText.contains("&")) {
                        drawRoleText = drawRoleText.replace(drawRoleText.substring(drawRoleText.indexOf("&"), drawRoleText.indexOf("&") + 2), "");
                    }
                    while (drawRoleText.contains("§")) {
                        drawRoleText = drawRoleText.replace(drawRoleText.substring(drawRoleText.indexOf("§"), drawRoleText.indexOf("§") + 2), "");
                    }
                } else {
                    drawRoleText = drawRoleText.replace("&", "§");
                }
                drawRoleText = FontStyles.SemiBold.getFontContainer().getTextFont().trimStringToWidth(drawRoleText, 150, false);
                if (ConfigHandler.useColouredBg)
                    DrawUtil.drawRoundedSquare(xVal + 15 * minScale, yVal + 8 * minScale,
                            Math.min(FontStyles.SemiBold.getFontContainer().width(drawRoleText) * 0.17F * minScale + 6 * minScale, 51 * minScale), 5 * minScale, 1 * minScale,
                                    hex2Rgb(ConfigHandler.getColourByRole(ob.getRole())).getRGB(), alpha);

                DrawUtil.drawStringWithDepth(FontStyles.SemiBold.getFontContainer(), drawRoleText,
                        ConfigHandler.useColouredBg ? xVal + 18 * minScale : xVal + 15 * minScale, yVal + 10.5f * minScale, 0.17F * minScale, Color.WHITE.getRGB());
            }
            a.incrementAndGet();
        }
        //main render
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        //popmatrix glscissor
        GL11.glPopMatrix();
    }

    private void drawBackground(int widthScreen, int heightScreen, int ping, int online){
        DrawUtil.drawImage(widthScreen / 2 - 150 * minScale, heightScreen / 2 - 80 * minScale, 300 * minScale, 160 * minScale, ResourcesProxy.bigWindow, alpha);
        DrawUtil.drawRectFloat(widthScreen/2-140*minScale , heightScreen/2 - 60*minScale,
                widthScreen/2+140*minScale, heightScreen/2 - 61*minScale, Color.WHITE.getRGB(), alpha);
        DrawUtil.drawStringWithDepth(FontStyles.SemiBold.getFontContainer(),FontStyles.SemiBold.getFontContainer().getTextFont().trimStringToWidth(ConfigHandler.tabHeader,350,false) ,
                widthScreen/2-140*minScale, heightScreen/2 - 69*minScale,0.3F*minScale, Color.WHITE.getRGB());
        //ping
        drawPing(widthScreen,heightScreen, ping);
        //tps and online
        if(ConfigHandler.drawTPS){
            DrawUtil.drawStringWithDepth(FontStyles.SemiBold.getFontContainer(), "TPS: ",
                    widthScreen/2+5*minScale, heightScreen/2 - 69*minScale,0.25F*minScale, Color.WHITE.getRGB());
            DrawUtil.drawStringWithDepth(FontStyles.SemiBold.getFontContainer(), String.valueOf(tps),
                    widthScreen/2+23*minScale, heightScreen/2 - 69*minScale,0.25F*minScale, getTpsColor(Integer.valueOf(tps)));
            DrawUtil.drawStringWithDepth(FontStyles.SemiBold.getFontContainer(),"|",
                    widthScreen/2+41*minScale, heightScreen/2 - 69*minScale,0.15F*minScale, Color.WHITE.getRGB());
        }
        DrawUtil.drawStringWithDepth(FontStyles.SemiBold.getFontContainer(),"Online: ",
                widthScreen/2+46*minScale, heightScreen/2 - 69*minScale,0.25F*minScale, Color.WHITE.getRGB());
        DrawUtil.drawStringWithDepth(FontStyles.SemiBold.getFontContainer(),String.valueOf(online),
                widthScreen/2+70*minScale, heightScreen/2 - 69*minScale,0.25F*minScale, hex2Rgb("#498f03").getRGB());
    }
    private void drawPing(int widthScreen, int heightScreen, int ping){
        DrawUtil.drawRectFloat(widthScreen/2+132*minScale , heightScreen/2 -66*minScale,
                widthScreen/2+134*minScale, heightScreen/2 - 69*minScale, getPingColor(ping,1), alpha);
        DrawUtil.drawRectFloat(widthScreen/2+135*minScale , heightScreen/2 -66*minScale,
                widthScreen/2+137*minScale, heightScreen/2 - 71*minScale, getPingColor(ping,2), alpha);
        DrawUtil.drawRectFloat(widthScreen/2+138*minScale , heightScreen/2 -66*minScale,
                widthScreen/2+140*minScale, heightScreen/2 -73*minScale, getPingColor(ping,3), alpha);
        DrawUtil.drawStringWithDepth(FontStyles.SemiBold.getFontContainer(), String.valueOf(ping),widthScreen/2+130*minScale , heightScreen/2 -74*minScale,
                0.15F*minScale, hex2Rgb("#525252").getRGB());
    }
    private int getPingColor(int ping, int nump){
        if(ping>150){
            if(nump>1)
                return hex2Rgb("#525252").getRGB();
            return hex2Rgb("#eb4034").getRGB();
        }else if(ping>80){
            if(nump>2)
                return hex2Rgb("#525252").getRGB();
            return hex2Rgb("#eb8f34").getRGB();
        }else{
            return hex2Rgb("#498f03").getRGB();
        }
    }
    private int getTpsColor(int tps){
        if(tps<10){
            return hex2Rgb("#eb4034").getRGB();
        }else if(tps<15){
            return hex2Rgb("#eb8f34").getRGB();
        }else{
            return hex2Rgb("#498f03").getRGB();
        }
    }
    private Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
                Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
                Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
    }
    private void recalculateScale(int widthScreen, int heightScreen) {
        this.scaleX = widthScreen / this.referenceWidth;
        this.scaleY = heightScreen / this.referenceHeight;
        float minSc = Math.min(this.scaleX, this.scaleY);
        this.minScale = minSc;
    }
    private void glScissor(float x, float y, float width, float height) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int scale = resolution.getScaleFactor();

        float scissorWidth = width * scale;
        float scissorHeight = height * scale;
        float scissorX = x * scale;
        float scissorY = mc.displayHeight - scissorHeight - (y * scale);
        GL11.glScissor((int) scissorX, (int) scissorY, (int) scissorWidth, (int) scissorHeight);
    }
    @SubscribeEvent
    public void rightClick(MouseEvent event) {
        //if gui not opened or not enough player in list to scroll
        if(alpha==0.0 || maxScrollOffset==0)
            return;
        int dw = event.dwheel;
        event.setCanceled(true);
        if(dw != 0) {
            if (dw > 0) {
                dw = -1;
            } else {
                dw = 1;
            }
            float amountScrolled = (float) (dw * 5);
            if(scrollOffset + amountScrolled > maxScrollOffset)
                scrollOffset = (int) (maxScrollOffset);
            else if (scrollOffset + amountScrolled > 0)
                scrollOffset += amountScrolled;
            else
                scrollOffset = 0;
        }
    }
}
