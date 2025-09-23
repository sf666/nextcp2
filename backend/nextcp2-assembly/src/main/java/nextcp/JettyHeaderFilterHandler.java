package nextcp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.Callback;


public class JettyHeaderFilterHandler extends Handler.Wrapper {

	public JettyHeaderFilterHandler() {
	}

	public JettyHeaderFilterHandler(boolean dynamic) {
		super(dynamic);
	}

	public JettyHeaderFilterHandler(Handler arg0) {
		super(arg0);
	}

	public JettyHeaderFilterHandler(boolean dynamic, Handler handler) {
		super(dynamic, handler);
	}

	@Override
	public boolean handle(Request request, Response response, Callback callback) throws Exception {
		boolean handeled =  super.handle(request, response, callback);

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss");
		
		response.getHeaders().remove("Last-Modified");
		response.getHeaders().add("Last-Modified", dateFormat.format(System.currentTimeMillis()));
		return handeled;
	}
	
}
