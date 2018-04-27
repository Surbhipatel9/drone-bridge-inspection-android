package xyz.maxime_brgt.testretrofit;

public class Photo {
    private int photoID;
    private String filepath;
    private String name;
    private String description;

    public Photo(){

    }

    public Photo(String filepath, String name, String description){
        this.filepath = filepath;
        this.name = name;
        this.description = description;
    }

    public void setID(int photoID){
        this.photoID = photoID;
    }

    public void setFilepath(String filepath){
        this.filepath = filepath;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public int getID(){
        return this.photoID;
    }

    public String getName(){
        return this.name;
    }

    public String getFilepath(){
        return this.filepath;
    }

    public String getDescription(){
        return this.description;
    }
}
