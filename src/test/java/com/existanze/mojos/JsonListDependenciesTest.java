package com.existanze.mojos;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;

/**
 * If no license is here then you can whatever you like!
 * and of course I am not liable
 * <p/>
 * Created by fotis on 05/01/15.
 */

public class JsonListDependenciesTest extends TestCase{
    /**
     * @throws Exception
     */

    @Test
    public void testMojoGoal() throws Exception
    {

        JsonListDependencies jl = new JsonListDependencies();
        jl.setJars(new File("repo"));
        jl.setOutput(new File("out.json"));
        jl.execute();
    }
}
