package com.colvis;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class CollectionVisualizer <T>{

    private String pathToSaveImages;
    private String fileType;
    private String fileName;
    private String fullFilePath;
    private String directoryName;
    private static int borderSize = 10;
    private static int widthOfCell = 350 + borderSize;
    private static int heightOfCell = 200 + borderSize;
    private static Color color = Color.BLUE;
    private final static Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    //Constructor with path to save images
    public CollectionVisualizer(String pathToSaveImages, String fileType, String fileName) {
        this.pathToSaveImages = pathToSaveImages;
        this.fileType = fileType;
        this.fileName = fileName;
        this.directoryName = this.pathToSaveImages + "\\" + this.fileName;
        this.fullFilePath = this.directoryName + "\\" + this.fileName + "." + this.fileType;
    }

    //constructor without path to save images
    public CollectionVisualizer(){
        this.pathToSaveImages = null;
        this.fileType = null;
        this.fileName = null;
    }

    public String getFullFilePath(){
        LocalDateTime localDateTime = LocalDateTime.now();
        return this.directoryName + "\\" + this.fileName + "_" + localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd_HH-mm")) + "." + this.fileType;
    }


    //getter for pathToSaveImages
    public String getPathToSaveImages() {
        return pathToSaveImages;
    }

    //setter for pathToSaveImages
    public void setPathToSaveImages(String pathToSaveImages) {
        this.pathToSaveImages = pathToSaveImages;
    }

    //getter for borderSize
    public static int getBorderSize(){
        return borderSize;
    }

    //setter for borderSize
    public static void setBorderSize(int borderSize) {
        CollectionVisualizer.borderSize = borderSize;
        setWidthOfCell(widthOfCell+borderSize);
        setHeightOfCell(heightOfCell+borderSize);
    }

    //getter for widthOfCell
    public static int getWidthOfCell() {
        return widthOfCell;
    }

    //getter for heightOfCell
    public static int getHeightOfCell() {
        return heightOfCell;
    }

    //setter for widthOfCell
    public static void setWidthOfCell(int widthOfCell) {
        CollectionVisualizer.widthOfCell = widthOfCell + getBorderSize();
    }

    //setter for heightOfCell
    public static void setHeightOfCell(int heightOfCell) {
        CollectionVisualizer.heightOfCell = heightOfCell + getBorderSize();
    }

    //getter for color
    public static Color getColor() {
        return color;
    }

    //setter for color
    public static void setColor(Color color) {
        CollectionVisualizer.color = color;
    }

    //converter for use in converting all passed in collections into a Map<T, T>
    final private Converter converter = new Converter();

    //Public call methods to convert collections and run the code
    public void displayCollection(int heightOfCell, int widthOfCell, Font fontUsed, Iterable<T> list) throws IOException {
        Map<T, T> passedList = converter.converter(list);
        displayArrayList(heightOfCell, widthOfCell, passedList);
    }

    public void displayCollection(int heightOfCell, int widthOfCell, Font fontUsed, Object[] array) throws IOException {
        Map<T, T> passedArray = converter.converter(List.of(array));
        displayArrayList(heightOfCell, widthOfCell, passedArray);
    }

    public void displayCollection(int heightOfCell, int widthOfCell, Map<T,T> map) throws IOException {
        displayArrayList(heightOfCell, widthOfCell, map);
    }

    public void displayCollection(Iterable<T> list) throws IOException {
        Map<T, T> passedList = converter.converter(list);
        displayArrayList(heightOfCell, widthOfCell, passedList);
    }

    public void displayCollection(Font fontUsed, Object[] array) throws IOException {
        Map<T, T> passedArray = converter.converter(List.of(array));
        displayArrayList(heightOfCell, widthOfCell, passedArray);
    }

    public void displayCollection(Font fontUsed, Map<T,T> map) throws IOException {
        displayArrayList(heightOfCell, widthOfCell, map);
    }

    private void displayArrayList(int heightOfCell, int widthOfCell, Map<T, T> map) throws IOException {

        boolean saveImage = this.pathToSaveImages != null;

        //creates the layout of the output into two, infinite columns
        GridLayout gridLayout = new GridLayout(0, 2);

        //Create an ArrayList of JTextArea's and set all properties of them at creation
        //List<JTextArea> jTextAreas = new ArrayList<>();
        List<ElementDisplayer> elementDisplayerList = new ArrayList<>();

        //Add all Indexes/Keys into A single list to later be placed into jPanel
        int length = 0;
        Set<T> keySet = map.keySet();
        for(T key : keySet){
            elementDisplayerList.add(new ElementDisplayer(map.get(key).toString(), borderSize, color));
            length++;
        }
        //Create a new JPanel using the GridLayout Created earlier
        JPanel jPanel = new JPanel(gridLayout);

        //Insert headers into above each Index and Each Value
        jPanel.add(new ElementDisplayer("Indexes", borderSize, color));
        jPanel.add(new ElementDisplayer("Values", borderSize, color));

        //Iterate through Each created JTextArea and add it into the JPanel
        for(int i = 0; i < elementDisplayerList.size(); i++){
            jPanel.add(new ElementDisplayer(Integer.toString(i), borderSize, color));
            jPanel.add(elementDisplayerList.get(i));
        }

        //create the JFrame for displaying data
        JFrame frame = new JFrame("Collection Visualizer");

        //sets a JScrollPane for scroll bar as needed
        JScrollPane jScrollPane = new JScrollPane(jPanel);
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        //Set size and default operation for JFrame, and Add jScrollPane to JFrame
        frame.add(jScrollPane);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //set the size of the frame to be smaller than the total dimension of the screen
        frame.setSize(new Dimension((int) (SCREEN_SIZE.getWidth() - widthOfCell), (int) (SCREEN_SIZE.getHeight() - heightOfCell)));
        //Display Frame
        frame.setVisible(true);


        if(saveImage){
            //set a new image to be saved, with a height and width of the total length of the data set and save the complete data set to the BufferedImage
            BufferedImage collection = new BufferedImage(jPanel.getWidth(), jPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = collection.createGraphics();
            jPanel.paint(graphics2D);


            try {
                //if the directory to store images isn't created, create it
                if(!new File(this.directoryName).isDirectory()){
                    System.out.println(new File(this.directoryName).mkdir());
                }
                //save the file into the directory
                ImageIO.write(collection, fileType, new File(this.getFullFilePath()));
            } catch (Exception exception){
                System.out.println(exception.getMessage());
            }
        }
    }
}
