import org.junit.Assert;
import org.junit.Test;

public class Testas {

    @Test
    public void testTikrinti() throws Exception{
        boolean result = Main.arYra("labas, mano vardas Ciklopas");

        Assert.assertEquals(false, result);
    }
}
