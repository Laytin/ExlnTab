package com.laytin.exlntab.handlers;

import com.laytin.exlntab.render.PhotoRender;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class ListenerClient {
    public static Map<String,String> playerList = new HashMap<>();
    protected final Minecraft mc = Minecraft.getMinecraft();
    private float height = 50.0f;
    private float width = 50.0f;
    @SubscribeEvent(
            priority = EventPriority.HIGH
    )
    public void eventHandler(RenderGameOverlayEvent.Pre event) {
        if (event.type == RenderGameOverlayEvent.ElementType.PLAYER_LIST) {
            event.setCanceled(true);
        }
        ScoreObjective scoreobjective = this.mc.theWorld.getScoreboard().func_96539_a(0);
        NetHandlerPlayClient handler = this.mc.thePlayer.sendQueue;
        int widthScreen = event.resolution.getScaledWidth();
        int heightScreen = event.resolution.getScaledHeight();
        if (this.mc.gameSettings.keyBindPlayerList.getIsKeyPressed() && (!this.mc.isIntegratedServerRunning()
            || handler.playerInfoList.size() > 1 || scoreobjective != null)
            && event.type == RenderGameOverlayEvent.ElementType.TEXT) {
            //Cancel event
            if (event.type == RenderGameOverlayEvent.ElementType.PLAYER_LIST) {
                event.setCanceled(true);
            }

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
    private void drawBackground(int widthScreen, int heightScreen){
        
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
