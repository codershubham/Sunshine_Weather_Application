package model;

/**
 * Created by shubham on 14-10-2016.
 */
public class Weather {
    public Place place ;
    public String iconData;
    public CurrentCondition currentcondition = new CurrentCondition();
    public Temperature temperature = new Temperature();
    public Wind wind = new Wind();
    public Snow snow = new Snow();
    public Clouds clouds = new Clouds();

}
