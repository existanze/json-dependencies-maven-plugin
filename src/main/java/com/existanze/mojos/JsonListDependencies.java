package com.existanze.mojos;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.plexus.util.IOUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

@Mojo(name="create", requiresDependencyResolution= ResolutionScope.COMPILE)
public class JsonListDependencies
    extends AbstractMojo
{

    @Parameter(name="jars",required = true)
    private File jars;

    @Parameter(name="output",required = true)
    private File output;


    public void execute()
        throws MojoExecutionException
    {

        getLog().info("Starting to create list "+jars);

        long total = 0;
        Map<String,Object> jsonMap= new HashMap<String,Object>();


        jsonMap.put("compiled",String.valueOf(System.currentTimeMillis()));



            List<Map<String,Object>> fileObjects = new ArrayList<Map<String,Object>>();
            File[] files = jars.listFiles();
            String filesStr="";

            if(files == null){
                return;
            }


            for(File f: files){


                if(f.getAbsolutePath().endsWith(".jar")){


                    String jar = f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf("/")+1);
                    String jarName= jar.replaceAll(".jar","");

                    Map<String,Object> fO = new HashMap<String, Object>();
                    fO.put("name",jarName);
                    fO.put("path","repo"+f.getAbsolutePath().replaceAll(jars.getAbsolutePath(),""));
                    fO.put("fileSize",f.length());
                    fO.put("lastModified",f.lastModified());

                    total += f.length();

                    fileObjects.add(fO);
                }
            }

        jsonMap.put("libs",fileObjects);
        jsonMap.put("total",total);


        FileWriter fileWriter = null;
        try {

            ObjectMapper maper = new ObjectMapper();
            maper.writeValue(output,jsonMap);
        } catch (IOException e) {
            e.printStackTrace();
            throw new MojoExecutionException("Couldn't write file "+output,e);
        }finally {
            IOUtil.close(fileWriter);
        }

        getLog().info("Total bytes " + total);

    }

    public void setJars(File jars) {
        this.jars = jars;
    }

    public void setOutput(File output) {
        this.output = output;
    }
}
