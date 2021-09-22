package nextcp.eventBridge;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import nextcp.dto.FileChangedEventDto;

@Controller
public class UdpMulticastReceiver {

	private static final Logger log = LoggerFactory.getLogger(UdpMulticastReceiver.class.getName());
	private boolean isRunning = true;

	@Autowired
	private ApplicationEventPublisher publisher = null;
	
	public UdpMulticastReceiver() {
		receiveMessage();
	}

	public void stop() {
		isRunning = false;
	}

	private void receiveMessage() {

		Runnable receiver = new Runnable() {

			@Override
			public void run() {
				int mcPort = 59263;
				MulticastSocket mcSocket = null;
				try {
					while (isRunning) {
						mcSocket = new MulticastSocket(mcPort);
						log.info("Multicast Receiver running at:" + mcSocket.getLocalSocketAddress());
						DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
						mcSocket.receive(packet);
						String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());
						String type = msg.substring(0, 1);
						String val = msg.substring(1);
						publisher.publishEvent(new FileChangedEventDto(type, val));
					}
				} catch (IOException e) {
					log.error("MulticastReceiver", e);
				}
				mcSocket.close();
			}
		};

		Thread receiveThreaad = new Thread(receiver);
		receiveThreaad.setDaemon(true);
		receiveThreaad.setName("Udp-Multicast-Receiver");
		receiveThreaad.start();
	}
}
