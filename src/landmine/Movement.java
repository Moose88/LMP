package landmine;

import jig.Entity;
import jig.Vector;

public class Movement extends Entity {

    private int angle;

    public void Movement(String direction, Vector position){
        if(direction == "up"){
            angle = 90;
        } else if(direction == "down"){
            angle = 270;
        } else if(direction == "left"){
            angle = 0;
        } else if(direction == "right"){
            angle = 180;
        }

        Vector v = new Vector(position);
        v.setRotation(angle).unit();
        setPosition(position.add(v.scale(0.016f)));

        // set position, get position, add, v.scale 0.016 *delta
    }
}
