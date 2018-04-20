package util;

import org.junit.Test;

import static org.junit.Assert.*;

public class IdentifierTest {

    @Test
    public void generateIdentifier() {
        int count = 5;
        for (int i = 0; i < count; i++){
            Identifier id = Identifier.generateIdentifier();
        }
        assertEquals(6, Identifier.generateIdentifier().asLong());
    }

    @Test
    public void createIdentifierZero() {
        assertEquals(0, Identifier.createIdentifierZero().asLong());
    }
}