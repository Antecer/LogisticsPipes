package logisticspipes.network.packets.debuggui;

import java.io.IOException;

import logisticspipes.network.LPDataInputStream;
import logisticspipes.network.LPDataOutputStream;
import logisticspipes.network.abstractpackets.ModernPacket;
import logisticspipes.ticks.DebugGuiTickHandler;

import net.minecraft.entity.player.EntityPlayer;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class DebugSetVarContent extends ModernPacket {

	@Getter
	@Setter
	private String content;

	@Getter
	@Setter
	private Integer[] path;

	public DebugSetVarContent(int id) {
		super(id);
	}

	@Override
	public void readData(LPDataInputStream data) throws IOException {
		content = data.readUTF();
		int size = data.readInt();
		path = new Integer[size];
		for (int i = 0; i < size; i++) {
			path[i] = data.readInt();
		}
	}

	@Override
	public void processPacket(EntityPlayer player) {
		try {
			DebugGuiTickHandler.instance().handleVarChangePacket(path, content, player);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void writeData(LPDataOutputStream data) throws IOException {
		data.writeUTF(content);
		data.writeInt(path.length);
		for (Integer element : path) {
			data.writeInt(element);
		}
	}

	@Override
	public ModernPacket template() {
		return new DebugSetVarContent(getId());
	}

	@Override
	public boolean isCompressable() {
		return true;
	}
}
