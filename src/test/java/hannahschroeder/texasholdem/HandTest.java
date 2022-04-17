package hannahschroeder.texasholdem;

import org.junit.Assert;
import org.junit.Test;

public class HandTest {
    
    @Test
    public void checkHandComparing()
    {
        Hand hand1 = Hand.parse("J♣ 5♥ K♦ Q♠ 5♣");
        Hand hand2 = Hand.parse("A♦ 5♥ K♦ Q♠ 5♣");
        
        Assert.assertTrue(hand1.compareTo(hand2) < 0);
    }
}
