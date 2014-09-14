package org.anc.tutorial.rest

import org.junit.*
import static org.junit.Assert.*

/**
 * @author Keith Suderman
 */
class SimpleServiceTest {
    @Test
    void testGreet() {
        assertTrue "Hello world" == new SimpleService().greet('world')
    }
}
