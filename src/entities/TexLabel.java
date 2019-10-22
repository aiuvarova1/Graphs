package entities;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import main.Drawer;
import org.jfree.fx.FXGraphics2D;
import org.scilab.forge.jlatexmath.*;


public class TexLabel extends Canvas {

    private FXGraphics2D gc;

    public TexLabel(){
        super();
        setLayoutX(50);
        setLayoutY(70);
        setText("\\infty");

        if(Graph.getInstance().areDistancesShown())
            show();
    }

    public void setText(String text) {
        this.gc = new FXGraphics2D(getGraphicsContext2D());
        this.gc.scale(100, 100);

        // create a formula
        TeXFormula formula = null;
        try {
            formula = new TeXFormula("1" + text);
        }catch(ParseException e){
            formula = new TeXFormula("1" + "\\infty");
        }


        TeXIcon texIcon = formula.createTeXIcon(TeXConstants.ALIGN_CENTER,25);

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        double width = texIcon.getIconWidth();
        double height = texIcon.getIconHeight();

        setWidth(width);
        setHeight(height);

        gc.clearRect(0, 0, width*10, height);

        FXGraphics2D graphics = new FXGraphics2D(gc);
        texIcon.paintIcon(null, graphics, 0, 0);
//        getGraphicsContext2D().clearRect(0, 0, icon.getIconWidth(), icon.getIconHeight());
   //     this.box.draw(graphics, 1, 5);

    }

    public void show(){
        System.out.println("show");
        Drawer.getInstance().addElem(this);
    }

    public void hide(){
        Drawer.getInstance().removeElement(this);
    }
}
