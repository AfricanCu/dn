package gm.server.logic;

import gm.server.logic.config.ServerTemplate;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import msg.InnerMessage.GsLoginBusHttp;

import com.google.protobuf.MessageLite;
import com.wk.bean.NTxtAbs;
import com.wk.engine.net.InnerMsgId;
import com.wk.engine.util.ProtobufUtils;

/**
 * 内部servlet
 * 
 * @author lixing
 */
public class InnerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html; charset=utf8");
		DataInputStream input = new DataInputStream(req.getInputStream());
		DataOutputStream outputStream = new DataOutputStream(
				resp.getOutputStream());
		byte[] byteArray = null;
		InnerMsgId enm = null;
		try {
			enm = InnerMsgId.getEnum(input.readShort());
			byte[] bytes = new byte[input.readInt()];
			input.readFully(bytes);
			MessageLite proto = ProtobufUtils.getProto(bytes,
					enm.getDefaultInst());
			switch (enm) {
			case GsLoginBusHttp:
				byteArray = gsLoginBus((GsLoginBusHttp) proto);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (byteArray == null && enm != null) {
			byteArray = enm.gRErrMsg(NTxtAbs.COMMON_ERROR);
		}
		if (byteArray != null) {
			outputStream.writeInt(byteArray.length);
			outputStream.write(byteArray);
			outputStream.flush();
		}
	}

	private byte[] gsLoginBus(GsLoginBusHttp proto) {
		int serverId = proto.getServerId();
		ServerTemplate serverTemplate = ServerTemplate
				.getServerTemplate(serverId);
		if (serverTemplate == null) {
			return NTxtAbs.GSLOGINBUS_SERVER_TEMPLATE_EMPTY;
		}
		serverTemplate.checkClient();
		return NTxtAbs.GSLOGIN_BUS_SUCCESS;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

}