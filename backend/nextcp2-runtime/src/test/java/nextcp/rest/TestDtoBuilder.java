package nextcp.rest;

import static org.junit.Assert.assertTrue;

import org.fourthline.cling.support.model.Res;
import org.junit.jupiter.api.Test;

import nextcp.dto.AudioFormat;

public class TestDtoBuilder
{
    @Test
    public void testAudioDuration()
    {
        DtoBuilder db = new DtoBuilder();
        Res res = buildResObject("3:20"); // 3*60 + 20 = 200L
        AudioFormat af = db.extractAudioFormat(res);
        assertTrue(af.durationInSeconds.equals(200L));

        res = buildResObject("03:20"); // 3*60 + 20 = 200L
        af = db.extractAudioFormat(res);
        assertTrue(af.durationInSeconds.equals(200L));

        res = buildResObject("3:20.0"); // 3*60 + 20 = 200L
        af = db.extractAudioFormat(res);
        assertTrue(af.durationInSeconds.equals(200L));

        res = buildResObject("3:20.00"); // 3*60 + 20 = 200L
        af = db.extractAudioFormat(res);
        assertTrue(af.durationInSeconds.equals(200L));

        res = buildResObject("0:3:20.0"); // 3*60 + 20 = 200L
        af = db.extractAudioFormat(res);
        assertTrue(af.durationInSeconds.equals(3 * 60L + 20L));

        res = buildResObject("12:4:59.0"); // 3*60 + 20 = 200L
        af = db.extractAudioFormat(res);
        assertTrue(af.durationInSeconds.equals(12 * 60 * 60L + 4 * 60L + 59L));
    }

    private Res buildResObject(String duration)
    {
        Res res = new Res();
        res.setBitsPerSample(2L);
        res.setBitrate(192 * 125L);
        res.setSampleFrequency(1140L);
        res.setNrAudioChannels(2L);
        res.setDuration(duration);
        return res;
    }
}
