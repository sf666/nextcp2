package nextcp2.upnp.localdevice;

import java.net.URI;
import org.jupnp.support.avtransport.impl.AVTransportStateMachine;
import org.jupnp.support.avtransport.impl.state.AbstractState;
import org.jupnp.support.avtransport.impl.state.Stopped;
import org.jupnp.support.model.SeekMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Nextcp2StateMachineDefinition implements AVTransportStateMachine{

	private static final Logger log = LoggerFactory.getLogger(Nextcp2StateMachineDefinition.class.getName());
	
	public Nextcp2StateMachineDefinition() {
	}

	@Override
	public AbstractState<?> getCurrentState() {
		log.error("please implement ...");
		return null;
	}

	@Override
	public void forceState(Class<? extends AbstractState<?>> state) {
		log.debug("forceState " + state);
	}

	@Override
	public void setTransportURI(URI uri, String uriMetaData) {
		log.debug("setTransportURI " + uri);
	}

	@Override
	public void setNextTransportURI(URI uri, String uriMetaData) {
		log.debug("setNextTransportURI " + uri);
	}

	@Override
	public void stop() {
		log.debug("stop ");
	}

	@Override
	public void play(String speed) {
		log.debug("play ");
	}

	@Override
	public void pause() {
		log.debug("pause ");
	}

	@Override
	public void record() {
		log.debug("record ");
	}

	@Override
	public void seek(SeekMode unit, String target) {
		log.debug("seek ");
	}

	@Override
	public void next() {
		log.debug("next ");
	}

	@Override
	public void previous() {
		log.debug("previous ");
	}

}
