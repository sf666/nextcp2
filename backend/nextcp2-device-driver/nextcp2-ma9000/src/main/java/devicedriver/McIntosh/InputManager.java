package devicedriver.McIntosh;

import java.util.HashMap;

import nextcp.dto.InputSourceDto;

/**
 * <pre>
1 = BAL 1
2 = BAL 2
3 = UNBAL1
4 = UNBAL2
5 = UNBAL3
6 = UNBAL4
7 = UNBAL5
8 = UNBAL6
9 = MM PHONO
10 = MC PHONO
11 = COAX1
12 = COAX2
13 = OPTI1
14 = OPTI2
15 = USB
16 = MCT
 * </pre>
 */
public class InputManager
{
    private HashMap<Integer, InputSourceDto> inputs = new HashMap<>();

    public InputManager()
    {
        inputs.put(1, new InputSourceDto(1, "BAL 1", "", true));
        inputs.put(2, new InputSourceDto(2, "BAL 2", "", true));
        inputs.put(3, new InputSourceDto(3, "UNBAL 1", "", true));
        inputs.put(4, new InputSourceDto(4, "UNBAL 2", "", true));
        inputs.put(5, new InputSourceDto(5, "UNBAL 3", "", true));
        inputs.put(6, new InputSourceDto(6, "UNBAL 4", "", true));
        inputs.put(7, new InputSourceDto(7, "UNBAL 5", "", true));
        inputs.put(8, new InputSourceDto(8, "UNBAL 6", "", true));
        inputs.put(9, new InputSourceDto(9, "MM PHONO", "", true));
        inputs.put(10, new InputSourceDto(10, "MC PHONO", "", true));
        inputs.put(11, new InputSourceDto(11, "COAX 1", "", true));
        inputs.put(12, new InputSourceDto(12, "COAX 2", "", true));
        inputs.put(13, new InputSourceDto(13, "OPTI 1", "", true));
        inputs.put(14, new InputSourceDto(14, "OPTI 2", "", true));
        inputs.put(15, new InputSourceDto(15, "USB", "", true));
        inputs.put(16, new InputSourceDto(16, "MCT", "", true));
    }

    public InputSourceDto getInputSource(int id)
    {
        return inputs.get(id);
    }

}
