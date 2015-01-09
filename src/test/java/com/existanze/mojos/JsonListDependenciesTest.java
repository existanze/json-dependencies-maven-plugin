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
        jl.setIncludes(new String[]{
            "repo"
        });
        jl.setBaseUri("http://www.existanze.com/ortholand");
        jl.setOutput(new File("out.json"));
        jl.execute();
    }

    @Test
    public void testNoBaseUri() throws Exception
    {

        JsonListDependencies jl = new JsonListDependencies();
        jl.setIncludes(new String[]{
            "repo"
        });
        jl.setOutput(new File("out-no-base-uri.json"));
        jl.execute();
    }
}
