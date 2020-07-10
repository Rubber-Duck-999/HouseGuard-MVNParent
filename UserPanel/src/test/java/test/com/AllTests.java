package test.com;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestModel.class, TestTypes.class,
                TestConsumerTopic.class, TestJsonTypes.class, TestController.class
              })
public class AllTests
{

}
