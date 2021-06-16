package nextcp.lastfm.request;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.SortedSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nextcp.lastfm.ILastFmConfig;

@Service
public class Signer
{

    @Autowired
    private ILastFmConfig config = null;

    public Signer()
    {
    }

    public String signCall(SortedSet<Parameter> paramSet)
    {
        StringBuilder sb = new StringBuilder();
        for (Parameter parameter : paramSet)
        {
            sb.append(parameter.getAsString());
        }
        String messageToHash = String.format("%s%s", sb.toString(), config.getSharedSecret());
        String signature = getMd5(messageToHash);
        return signature;
    }

    public static String getMd5(String input)
    {
        try
        {

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32)
            {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
    }

}
