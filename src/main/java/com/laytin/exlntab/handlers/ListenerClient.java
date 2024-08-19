package com.laytin.exlntab.handlers;

import com.laytin.exlntab.proxy.ResourcesProxy;
import com.laytin.exlntab.render.DrawUtil;
import com.laytin.exlntab.render.PhotoRender;
import com.laytin.exlntab.render.font.FontMagicObj;
import com.laytin.exlntab.render.font.FontStyles;
import com.laytin.exlntab.utils.PlayerInfoObj;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@SideOnly(Side.CLIENT)
public class ListenerClient {
    //static public for packets
    public static Map<String, PlayerInfoObj> playerList = new HashMap<>();
    public static float tps = 20F;
    //
    private final Minecraft mc = Minecraft.getMinecraft();
    private float referenceWidth = 427.0F;
    private float referenceHeight = 240.0F;
    private float scaleX = 1.0F;
    private float scaleY = 1.0F;
    private float minScale = 1.0F;
    private float alpha =0.0f;
    private int scrollOffset =0;

    @SubscribeEvent(
            priority = EventPriority.HIGH
    )
    public void eventHandlerTabRender(RenderGameOverlayEvent.Pre event) {
        if(!Keyboard.isKeyDown(Keyboard.KEY_TAB) && alpha!=0.0f)
            alpha=0.0f;

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
            drawBackground(widthScreen,heightScreen,
                    players.stream().filter(f->f.name.equals(this.mc.getSession().getUsername())).findFirst().get().responseTime, players.size());
            drawScissorList(widthScreen,heightScreen,players);
/*            this.mc.mcProfiler.startSection("playerList");
            ArrayList players = new ArrayList(handler.playerInfoList);
            GL11.glPushMatrix();
            GL11.glDisable(2929);
            int i;
            for(i = 0; i < players.size(); ++i) {
                GuiPlayerInfo player = (GuiPlayerInfo)players.get(i);
                String playerName = player.name;
                PhotoRender.getInstance().drawAvatar(playerName, 50,50,50,50);
            }
            GL11.glPopMatrix();
            GL11.glEnable(2929);*/
        }
    }
    private void drawScissorList(int widthScreen, int heightScreen,ArrayList<GuiPlayerInfo> players){
        //pushmatrix to move it to float coords cuz scissor can be called only with int values
        GL11.glPushMatrix();
        GL11.glTranslatef(widthScreen/2-140*minScale, heightScreen/2 - 55*minScale, 0.0F);
        //glscissor
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        glScissor((widthScreen/2-140*minScale), (heightScreen/2 - 55*minScale),(minScale * 280F),  (minScale * 125F));
        //main render
        //playerList.put("Steve1111111111111111111111111111111111", new PlayerInfoObj("adm6", "Гл.Админ", "Steve1111111111111111111111111111111111",100));
        AtomicInteger a = new AtomicInteger();
        for(PlayerInfoObj ob : playerList.values()){
            float xVal = ((a.get())%4)*71.3F*minScale;
            float yVal = ((a.get())/4)*20*minScale+scrollOffset*minScale;
            DrawUtil.drawImage(xVal,yVal, 66*minScale,15*minScale,ResourcesProxy.buttonHover,alpha);
            //Avatar
            PhotoRender.getInstance().drawAvatar(ob.getUsername(), xVal+2*minScale,yVal+2*minScale,11*minScale,11*minScale,1);
            DrawUtil.drawStringWithDepth(FontStyles.OpenSans_Regular.getFontContainer(),FontStyles.OpenSans_Regular.getFontContainer().getTextFont().trimStringToWidth(ob.getUsername(),25,false),
                    xVal+15*minScale, yVal+4*minScale,0.3F*minScale, Color.WHITE.getRGB());

            if(ConfigHandler.useGroupName){
                //do smth
            }else {
                //do smth
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
        DrawUtil.drawImage(widthScreen/2-150*minScale, heightScreen/2 - 80*minScale,300*minScale, 160*minScale,ResourcesProxy.bigWindow, alpha);
        DrawUtil.drawRectFloat(widthScreen/2-140*minScale , heightScreen/2 - 60*minScale,
                widthScreen/2+140*minScale, heightScreen/2 - 61*minScale, Color.WHITE.getRGB(), alpha);
        DrawUtil.drawStringWithDepth(FontStyles.SemiBold.getFontContainer(),FontStyles.SemiBold.getFontContainer().getTextFont().trimStringToWidth(ConfigHandler.tabHeader,270,false) ,widthScreen/2-140*minScale, heightScreen/2 - 70*minScale,0.5F*minScale, Color.WHITE.getRGB());
        //ping
        drawPing(widthScreen,heightScreen, ping);
        //tps and online
        DrawUtil.drawStringWithDepth(FontStyles.SemiBold.getFontContainer(), "TPS: ",
                widthScreen/2+5*minScale, heightScreen/2 - 69*minScale,0.4F*minScale, Color.WHITE.getRGB());
        DrawUtil.drawStringWithDepth(FontStyles.SemiBold.getFontContainer(), String.valueOf(tps),
                widthScreen/2+20*minScale, heightScreen/2 - 69*minScale,0.4F*minScale, getTpsColor((int) tps));
        DrawUtil.drawStringWithDepth(FontStyles.SemiBold.getFontContainer(),"|",
                widthScreen/2+36*minScale, heightScreen/2 - 69*minScale,0.3F*minScale, Color.WHITE.getRGB());
        DrawUtil.drawStringWithDepth(FontStyles.SemiBold.getFontContainer(),"Online: ",
                widthScreen/2+40*minScale, heightScreen/2 - 69*minScale,0.4F*minScale, Color.WHITE.getRGB());
        DrawUtil.drawStringWithDepth(FontStyles.SemiBold.getFontContainer(),String.valueOf(online),
                widthScreen/2+62*minScale, heightScreen/2 - 69*minScale,0.4F*minScale, hex2Rgb("#498f03").getRGB());
        if(alpha<0.9)
            alpha+=0.1f;
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
    public static Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
                Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
                Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
    }
    protected void recalculateScale(int widthScreen, int heightScreen) {
        this.scaleX = widthScreen / this.referenceWidth;
        this.scaleY = heightScreen / this.referenceHeight;
        float minSc = Math.min(this.scaleX, this.scaleY);
        this.minScale = minSc;
    }
    public void glScissor(float x, float y, float width, float height) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int scale = resolution.getScaleFactor();

        float scissorWidth = width * scale;
        float scissorHeight = height * scale;
        float scissorX = x * scale;
        float scissorY = mc.displayHeight - scissorHeight - (y * scale);
        GL11.glScissor((int) scissorX, (int) scissorY, (int) scissorWidth, (int) scissorHeight);
    }
}
        /*ScoreObjective scoreobjective = this.mc.theWorld.getScoreboard().func_96539_a(0);
        NetHandlerPlayClient handler = this.mc.thePlayer.sendQueue;
        int width = event.resolution.getScaledWidth();
        int height = event.resolution.getScaledHeight();
        if (this.mc.gameSettings.keyBindPlayerList.getIsKeyPressed() && (!this.mc.isIntegratedServerRunning() || handler.playerInfoList.size() > 1 || scoreobjective != null) && event.type == ElementType.TEXT) {
            if (event.type == RenderGameOverlayEvent.ElementType.PLAYER_LIST) {
                event.setCanceled(true);
            }

            this.mc.mcProfiler.startSection("playerList");
            ArrayList players = new ArrayList(handler.playerInfoList);
            int columns = ConfigHandler.playersInARow;
            int playersPerPage = true;
            int columnWidth = 400 / columns;
            int columnHeight = true;
            int left = (width - columns * columnWidth) / 2;
            int border = true;
            int currentPage = 0;
            GL11.glPushMatrix();
            GL11.glDisable(2929);
            GuiIngame var10000 = Minecraft.getMinecraft().ingameGUI;
            GuiIngame.drawRect(left, 0, left + columnWidth * columns - 1, 19, -2147483648);

            int i;
            for(i = 0; i < players.size(); ++i) {
                int cellPtr = i - currentPage * 120;
                int xPos = left + cellPtr % columns * columnWidth;
                int yPos = 20 + cellPtr / columns * 18;
                if (i < players.size()) {
                    var10000 = Minecraft.getMinecraft().ingameGUI;
                    GuiIngame.drawRect(xPos, yPos, xPos + columnWidth - 1, yPos + 18 - 1, (new Color(158, 152, 152, 100)).getRGB());
                    GuiPlayerInfo player = (GuiPlayerInfo)players.get(i);
                    String playerName = player.name;
                    ScorePlayerTeam team = this.mc.theWorld.getScoreboard().getPlayersTeam(playerName);
                    String[] displayName = ScorePlayerTeam.formatPlayerName(team, playerName).replaceAll("&", ").split(" ");
                            String name = displayName.length == 1 ? displayName[0] : displayName[1];
                    String prefix = displayName.length > 1 ? displayName[0] : "";
                    int strWidthName = this.mc.fontRenderer.getStringWidth(name.replaceAll(", "")) / 2;
                    int strWidthPrefix = this.mc.fontRenderer.getStringWidth(prefix.replaceAll(", "")) / 2;
                    this.mc.fontRenderer.drawString(name, (columnWidth + 18) / 2 - strWidthName + xPos - 2, yPos + 5, 16777215);
                    if (this.bindFace(StringUtils.stripControlCodes(playerName))) {
                        RenderUtil.drawImage(xPos, yPos, 17, 17);
                    }
                }
            }

            i = (int)Math.ceil((double)players.size() / (double)columns) * 18 + 20;
            var10000 = Minecraft.getMinecraft().ingameGUI;
            GuiIngame.drawRect(left, i, left + columnWidth * columns - 1, i + 19, -2147483648);
            this.mc.ingameGUI.drawCenteredString(this.mc.fontRenderer, ConfigHandler.tabHeader.replace('&', ').replace("%online%", String.valueOf(players.size())).replace("%maxplayers%", String.valueOf(handler.currentServerMaxPlayers)), width / 2, 6, -1);
            this.mc.ingameGUI.drawCenteredString(this.mc.fontRenderer, ConfigHandler.tabFooter.replace('&', ').replace("%online%", String.valueOf(players.size())).replace("%maxplayers%", String.valueOf(handler.currentServerMaxPlayers)), width / 2, i + 6, -1);
                    GL11.glPopMatrix();
            GL11.glEnable(2929);
        }
*/
