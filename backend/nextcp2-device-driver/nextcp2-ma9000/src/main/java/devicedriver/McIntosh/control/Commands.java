package devicedriver.McIntosh.control;

/**
 * <pre>
 * Ein Command hat die Struktur:
 * 
 * (COMMAND PARAMS)
 * 
 * Das abschließende CR/LF ist optional. Die Kommando's ohne die Klammern setzen.
 * 
 * </pre>
 */
public class Commands
{
    public static CommandStructure QUERY_ALL_COMMANDS = new CommandStructure("QRY");
    
    public static CommandStructure POWER_OFF = new CommandStructure("PWR 0");
    public static CommandStructure POWER_ON = new CommandStructure("PWR 1");
    public static CommandStructure POWER_STATUS = new CommandStructure("PWR");
    
    public static CommandStructure VOLUME_UP_ONE_PERCENT = new CommandStructure("VOL U");
    public static CommandStructure VOLUME_DOWN_ONE_PERCENT = new CommandStructure("VOL D");
    public static CommandStructure VOLUME_SET_PERCENT = new CommandStructure("VOL", 1);
    public static CommandStructure VOLUME_STATUS = new CommandStructure("VOL");

    public static CommandStructure SELECT_INPUT = new CommandStructure("INP", 1);
    public static CommandStructure INPUT_STATUS = new CommandStructure("INP");

    public static CommandStructure MUTE_ON = new CommandStructure("MUT 1");
    public static CommandStructure MUTE_OFF = new CommandStructure("MUT 0");
    
}
