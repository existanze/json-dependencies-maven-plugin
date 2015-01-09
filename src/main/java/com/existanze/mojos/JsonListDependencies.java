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

    @Parameter(name="includes",required = true)
    private String[] includes;

    @Parameter(name="output",required = true)
    private File output;

    @Parameter(name="baseUri")
    private String baseUri;



    public Object[] getDirNode(String base, File file){


        if(baseUri==null){
            baseUri="";
        }

        if(!file.isDirectory()){

            String _relative = file.getAbsolutePath().replaceAll(base,"");
            String _file = _relative.substring(_relative.lastIndexOf("/")+1);

            Map<String,Object> fO = new HashMap<String, Object>();
            fO.put("name",_file);
            fO.put("path", baseUri+_relative);
            fO.put("fileSize", file.length());
            fO.put("lastModified",file.lastModified());
            return new Object[]{file.length(),Arrays.asList(fO)};
        }

        List<Map<String,Object>> fileObjects = new ArrayList<Map<String,Object>>();
        File[] files = file.listFiles();
        String filesStr="";

        if(files == null){
            return null;
        }


        long total = 0L;


        for(File f: files){

            if(f.isDirectory()){
                Object[] dirNode = getDirNode(base,f);
                if(dirNode !=null){
                    fileObjects.addAll((Collection<? extends Map<String, Object>>) dirNode[1]);
                    total += (Long)dirNode[0];
                }
            }else{

                String _relative = f.getAbsolutePath().replaceAll(base,"");
                String _file = _relative.substring(_relative.lastIndexOf("/")+1);


                Map<String,Object> fO = new HashMap<String, Object>();
                fO.put("name",f.getAbsolutePath().replaceAll(base,""));
                fO.put("path",baseUri+"/"+_relative);
                fO.put("fileSize",f.length());
                fO.put("lastModified",f.lastModified());

                total += f.length();
                fileObjects.add(fO);
            }
        }

        return new Object[]{total,fileObjects};

    }


    public void execute()
        throws MojoExecutionException
    {

        Map<String,Object> jsonMap= new HashMap<String,Object>();


        jsonMap.put("compiled",String.valueOf(System.currentTimeMillis()));


        long total=0L;
        List<Map<Object,String>> files = new ArrayList<Map<Object,String>>();
        for(String include: includes){

            File file = new File(include);
            Object[] dirNode = getDirNode(include,file);
            if(dirNode !=null){
                files.addAll((Collection<? extends Map<Object, String>>) dirNode[1]);
                total += (Long)dirNode[0];
            }
        }

        jsonMap.put("files",files);
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


    public void setIncludes(String[] includes) {
        this.includes = includes;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public void setOutput(File output) {
        this.output = output;
    }

}
