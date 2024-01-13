import java.awt.*;
import java.awt.event.*;
/*********************************************/

public class MyCalculator extends Frame
{

    public boolean setClear=true;
    double number, memValue;
    String op;

    String digitButtonText[] = {"7", "8", "9", "4", "5", "6", "1", "2", "3", "0", "+/-", "." };
    String operatorButtonText[] = {"/", "sqrt", "*", "%", "-", "1/X", "+", "!" ,"log(10)","^","log","=","SIN","COS","TAN"};
    String memoryButtonText[] = {"MC", "MR", "MS", "M+" };
    String specialButtonText[] = {"Backspc", "C", "CE" };

    MyDigitButton digitButton[]=new MyDigitButton[digitButtonText.length];
    MyOperatorButton operatorButton[]=new MyOperatorButton[operatorButtonText.length];
    MyMemoryButton memoryButton[]=new MyMemoryButton[memoryButtonText.length];
    MySpecialButton specialButton[]=new MySpecialButton[specialButtonText.length];

    Label displayLabel=new Label("0",Label.RIGHT);
    Label memLabel=new Label(" ",Label.RIGHT);

    final int FRAME_WIDTH=325,FRAME_HEIGHT=325;
    final int HEIGHT=30, WIDTH=30, H_SPACE=10,V_SPACE=10;
    final int TOPX=30, TOPY=50;

    ///////////////////////////
    MyCalculator(String frameText)//constructor
    {
        super(frameText);
        int tempX=TOPX, y=TOPY;
        displayLabel.setBounds(tempX,y,285,HEIGHT);
        displayLabel.setBackground(Color.BLACK);
        displayLabel.setForeground(Color.WHITE);
        add(displayLabel);

        memLabel.setBounds(TOPX,  TOPY+HEIGHT+ V_SPACE,WIDTH, HEIGHT);
        add(memLabel);
        Panel buttonPanel = new Panel(new GridLayout(5, 1, H_SPACE, V_SPACE));
// set Co-ordinates for Memory Buttons
        tempX=TOPX;
        y=TOPY+2*(HEIGHT+V_SPACE);
        for(int i=0; i<memoryButton.length; i++)
        {
            memoryButton[i]=new MyMemoryButton(tempX,y,WIDTH,HEIGHT,memoryButtonText[i], this);
            memoryButton[i].setForeground(Color.BLACK);
            y+=HEIGHT+V_SPACE;
        }

//set Co-ordinates for Special Buttons
        tempX=TOPX+2*(WIDTH+H_SPACE); y=TOPY+1*(HEIGHT+V_SPACE);
        for(int i=0;i<specialButton.length;i++)
        {
            specialButton[i]=new MySpecialButton(tempX,y,WIDTH*2,HEIGHT,specialButtonText[i], this);
            specialButton[i].setForeground(Color.BLACK);
            tempX=tempX+2*WIDTH+H_SPACE;
        }

//set Co-ordinates for Digit Buttons
        int digitX=TOPX+WIDTH+H_SPACE;
        int digitY=TOPY+2*(HEIGHT+V_SPACE);
        tempX=digitX;  y=digitY;
        for(int i=0;i<digitButton.length;i++)
        {
            digitButton[i]=new MyDigitButton(tempX,y,WIDTH,HEIGHT,digitButtonText[i], this);
            digitButton[i].setForeground(Color.WHITE);
            tempX+=WIDTH+H_SPACE;
            if((i+1)%3==0){tempX=digitX; y+=HEIGHT+V_SPACE;}
        }

//set Co-ordinates for Operator Buttons
        int opsX=digitX+2*(WIDTH+H_SPACE)+H_SPACE;
        int opsY=digitY;
        tempX=opsX;  y=opsY;
        for(int i=0;i<operatorButton.length;i++)
        {
            tempX+=WIDTH+H_SPACE;
            operatorButton[i]=new MyOperatorButton(tempX,y,WIDTH,HEIGHT,operatorButtonText[i], this);
            operatorButton[i].setForeground(Color.WHITE);
            if((i+1)%3==0){tempX=opsX; y+=HEIGHT+V_SPACE;}
        }

        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent ev)
            {System.exit(0);}
        });

        setLayout(null);
        setSize(330,330);
        this.setBackground(Color.LIGHT_GRAY);
        setVisible(true);

    }
    //////////////////////////////////
    static String getFormattedText(double temp)
    {
        String resText=""+temp;
        if(resText.lastIndexOf(".0")>0)
            resText=resText.substring(0,resText.length()-2);
        return resText;
    }
    ////////////////////////////////////////
    public static void main(String []args)
    {
        new MyCalculator("Calculator - JavaTpoint");
    }
}

/*******************************************/
class RoundButton extends Button {
    private boolean isPressed = false;

    RoundButton(String label) {
        super(label);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        Dimension size = getSize();

        if (isPressed) {
            g.setColor(Color.lightGray);
        } else {
            g.setColor(getBackground());
        }
        g.fillRoundRect(0, 0, size.width - 1, size.height - 1, 10, 10);

        // Tambahkan kode untuk menggambar teks
        g.setColor(getForeground());
        FontMetrics fm = g.getFontMetrics();
        int textX = (size.width - fm.stringWidth(getLabel())) / 2;
        int textY = (size.height - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(getLabel(), textX, textY);

        // Jangan panggil super.paint(g) karena kita menggambar teks sendiri
    }
}

class MyDigitButton extends RoundButton implements ActionListener {
    MyCalculator cl;

    MyDigitButton(int x, int y, int width, int height, String cap, MyCalculator clc) {
        super(cap);
        setBounds(x, y, width, height);
        this.cl = clc;
        this.cl.add(this);
        addActionListener(this);
        setBackground(Color.DARK_GRAY);
    }

    ////////////////////////////////////////////////
    static boolean isInString(String s, char ch)
    {
        for(int i=0; i<s.length();i++) if(s.charAt(i)==ch) return true;
        return false;
    }
    /////////////////////////////////////////////////
    public void actionPerformed(ActionEvent ev)
    {
        String tempText=((MyDigitButton)ev.getSource()).getLabel();

        if(tempText.equals("."))
        {
            if(cl.setClear)
            {cl.displayLabel.setText("0.");cl.setClear=false;}
            else if(!isInString(cl.displayLabel.getText(),'.'))
                cl.displayLabel.setText(cl.displayLabel.getText()+".");
            return;
        }

        int index=0;
        try{
            index=Integer.parseInt(tempText);
        }catch(NumberFormatException e){return;}

        if (index==0 && cl.displayLabel.getText().equals("0")) return;

        if(cl.setClear)
        {cl.displayLabel.setText(""+index);cl.setClear=false;}
        else
            cl.displayLabel.setText(cl.displayLabel.getText()+index);

    }//actionPerformed
}//class defination

/********************************************/

class MyOperatorButton extends RoundButton implements ActionListener {
    MyCalculator cl;

    MyOperatorButton(int x, int y, int width, int height, String cap, MyCalculator clc) {
        super(cap);
        setBounds(x, y, 36, height);
        this.cl = clc;
        this.cl.add(this);
        addActionListener(this);
        setBackground(Color.DARK_GRAY);
    }
    ///////////////////////
    public void actionPerformed(ActionEvent ev)
    {
        String opText=((MyOperatorButton)ev.getSource()).getLabel();

        cl.setClear=true;
        double temp=Double.parseDouble(cl.displayLabel.getText());

        if(opText.equals("1/X"))
        {
            try
            {double tempd=1/(double)temp;
                cl.displayLabel.setText(MyCalculator.getFormattedText(tempd));}
            catch(ArithmeticException excp)
            {cl.displayLabel.setText("Divide by 0.");}
            return;
        }
        if(opText.equals("sqrt"))
        {
            try
            {double tempd=Math.sqrt(temp);
                cl.displayLabel.setText(MyCalculator.getFormattedText(tempd));}
            catch(ArithmeticException excp)
            {cl.displayLabel.setText("Divide by 0.");}
            return;
        }
        if(opText.equals("!"))
        {
            int faktorial = (int) temp;
            for (int i = (int) temp - 1; i > 0; i--) {
                faktorial *= i;
            }
            cl.displayLabel.setText(MyCalculator.getFormattedText(faktorial));
            return;
        }
        if (opText.equals("log(10)")) {
            double result = Math.log10(temp);
            cl.displayLabel.setText(MyCalculator.getFormattedText(result));
            return;
        }
        if(!opText.equals("="))
        {
            cl.number=temp;
            cl.op=opText;
            return;
//        }
    }

// process = button pressed
        switch(cl.op)
        {
            case "+":
                temp+=cl.number;break;
            case "-":
                temp=cl.number-temp;break;
            case "*":
                temp*=cl.number;break;
            case "%":
                try{temp=cl.number%temp;}
                catch(ArithmeticException excp)
                {cl.displayLabel.setText("Divide by 0."); return;}
                break;
            case "/":
                try{temp=cl.number/temp;}
                catch(ArithmeticException excp)
                {cl.displayLabel.setText("Divide by 0."); return;}
                break;
            case "^":
                temp=Math.pow(cl.number,temp);
                break;
            case "log":
                temp = Math.log(temp)/Math.log(cl.number);
            case "SIN":
                temp=Math.sin(Math.toRadians(temp));
                break;
            case "COS":
                temp=Math.cos(Math.toRadians(temp));
                break;
            case "TAN":
                temp=Math.tan(Math.toRadians(temp));
                break;
        }//switch

        cl.displayLabel.setText(MyCalculator.getFormattedText(temp));
//cl.number=temp;
    }//actionPerformed
}//class

/****************************************/

class MyMemoryButton extends RoundButton implements ActionListener {
    MyCalculator cl;

    MyMemoryButton(int x, int y, int width, int height, String cap, MyCalculator clc) {
        super(cap);
        setBounds(x, y, width, height);
        this.cl = clc;
        this.cl.add(this);
        addActionListener(this);
        setBackground(Color.DARK_GRAY);
    }
    ////////////////////////////////////////////////
    public void actionPerformed(ActionEvent ev)
    {
        char memop=((MyMemoryButton)ev.getSource()).getLabel().charAt(1);

        cl.setClear=true;
        double temp=Double.parseDouble(cl.displayLabel.getText());

        switch(memop)
        {
            case 'C':
                cl.memLabel.setText(" ");cl.memValue=0.0;break;
            case 'R':
                cl.displayLabel.setText(MyCalculator.getFormattedText(cl.memValue));break;
            case 'S':
                cl.memValue=0.0;
            case '+':
                cl.memValue+=Double.parseDouble(cl.displayLabel.getText());
                if(cl.displayLabel.getText().equals("0") || cl.displayLabel.getText().equals("0.0")  )
                    cl.memLabel.setText(" ");
                else
                    cl.memLabel.setText("M");
                break;
        }//switch
    }//actionPerformed
}//class

/*****************************************/

class MySpecialButton extends RoundButton implements ActionListener {
    MyCalculator cl;

    MySpecialButton(int x, int y, int width, int height, String cap, MyCalculator clc) {
        super(cap);
        setBounds(x, y, width, height);
        this.cl = clc;
        this.cl.add(this);
        addActionListener(this);
        setBackground(Color.DARK_GRAY);
    }
    //////////////////////
    static String backSpace(String s)
    {
        String Res="";
        for(int i=0; i<s.length()-1; i++) Res+=s.charAt(i);
        return Res;
    }

    //////////////////////////////////////////////////////////
    public void actionPerformed(ActionEvent ev)
    {
        String opText=((MySpecialButton)ev.getSource()).getLabel();
//check for backspace button
        if(opText.equals("Backspc"))
        {
            String tempText=backSpace(cl.displayLabel.getText());
            if(tempText.equals(""))
                cl.displayLabel.setText("0");
            else
                cl.displayLabel.setText(tempText);
            return;
        }
//check for "C" button i.e. Reset
        if(opText.equals("C"))
        {
            cl.number=0.0; cl.op=" "; cl.memValue=0.0;
            cl.memLabel.setText(" ");
        }

//it must be CE button pressed
        cl.displayLabel.setText("0");cl.setClear=true;
    }//actionPerformed
}//class
