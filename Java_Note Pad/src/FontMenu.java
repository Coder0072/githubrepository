import  javax.swing.*;
import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;
import java.awt.*;
import java.awt.event.*;

public class FontMenu extends JDialog{

    //will need referance to our GUI to makee the GUI from this class
    private NotepadGUI source;
    private JTextField currentfontfield,currentFontStyleField,currentSizeField;
    private JPanel currentColorBox;
    public FontMenu(NotepadGUI source){
        this.source=source;
        setTitle("Font Settings");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);//disposes used resource once closed
        setSize(425,350);
        setLocationRelativeTo(source);//launches the menu at center of our notepad GUI
        setModal(true);
        //removes layout management,giving us more control on the placement of our GUI components
        setLayout(null);

        addMenuComponents();

    }
    private void addMenuComponents(){
        addFontChooser();
        addFontStyleChooser();
        addFontSizeChooser();
        addFontColourChooser();
        //action buttons

        //apply on chnges on the fonts
        JButton applyButton=new JButton("Apply");
        applyButton.setBounds(230,265,75,25);
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get current font type
                String fontType= currentfontfield.getText();

                //get font style
                int fontStyle;
                switch (currentFontStyleField.getText()){
                    case "plain":
                        fontStyle=Font.PLAIN;
                        break;
                    case "Bold":
                        fontStyle=Font.BOLD;
                        break;
                    case"Italic":
                        fontStyle=Font.ITALIC;
                        break;
                    default://bold italic
                        fontStyle=Font.BOLD | Font.ITALIC;
                        break;

                }
                //get font size
                int fontSize=Integer.parseInt(currentSizeField.getText());

                //get font color
                Color fontColor=currentColorBox.getBackground();

                //create font
                Font newFont=new Font(fontType,fontStyle,fontSize);

                //update text area font
                source.getTextArea().setFont(newFont);

                //update text area font color
                source.getTextArea().setForeground(fontColor);

                //dispose the menu
                FontMenu.this.dispose();

            }
        });
        add(applyButton);

        //cancel button (exit menu)
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(315,265,75,25);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //dispose the menu
                FontMenu.this.dispose();
            }
        });
        add(cancelButton);


    }
    private void addFontChooser(){
        JLabel fontlabel=new JLabel("Font:");
        fontlabel.setBounds(10,5,125,10);
        add(fontlabel);

        //font panel will display the current font and the list of fonts available
        JPanel fontpanel= new JPanel();
        fontpanel.setBounds(10,15,125,160);

        //display current font avaiable
        currentfontfield =new JTextField(source.getTextArea().getFont().getFontName());
        currentfontfield.setPreferredSize(new Dimension(125,25));
        currentfontfield.setEditable(false);
        fontpanel.add(currentfontfield);

        //display list of available fonts
        JPanel listoffontspanel=new JPanel();

        //changes our layout to only have column to display each font properly
        listoffontspanel.setLayout(new BoxLayout(listoffontspanel,BoxLayout.Y_AXIS));

        //Change background colour to white
        listoffontspanel.setBackground(Color.WHITE);

        JScrollPane scrollPane=new JScrollPane(listoffontspanel);
        scrollPane.setPreferredSize(new Dimension(125,125));

        //retrive all of the possible fonts
        GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[]fontnames= ge.getAvailableFontFamilyNames();

        /* for each font name in font nams,we are going to display them to our
        listoffontpanel as a JLabel*/
        for(String fontName : fontnames){
            JLabel fontnameslabel = new JLabel(fontName);
            fontnameslabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    //when cliced set current fontfield to font name
                    currentfontfield.setText(fontName);
                }


                @Override
                public void mouseEntered(MouseEvent e) {
                    //add highlight over font names when the mouse hovers them
                    fontnameslabel.setOpaque(true);
                    fontnameslabel.setBackground(Color.BLUE);
                    fontnameslabel.setForeground(Color.WHITE);

                }

                @Override
                public void mouseExited(MouseEvent e) {
                    //remove the higghlight once the mouse stop hovering over the fot names
                    fontnameslabel.setBackground(null);//reset Background color
                    fontnameslabel.setForeground(null);

                }
            });

            //add to panel
            listoffontspanel.add(fontnameslabel);
        }
        fontpanel.add(scrollPane);

        add(fontpanel);
    }
    private void addFontStyleChooser(){
        JLabel fontstylelabel=new JLabel("Font Style:");
        fontstylelabel.setBounds(145,5,125,10);
        add(fontstylelabel);

        //will dissplay the current font style and all available font style
        JPanel fontstylePanel=new JPanel();
        fontstylePanel.setBounds(145,15,125,160);

       //get current font style
        int currentFontStyle=source.getTextArea().getFont().getStyle();
        String currentFontStyleText;

        switch (currentFontStyle){
            case Font.PLAIN:
                currentFontStyleText="plain";
                break;
            case Font.BOLD:
                currentFontStyleText="Bold";
                break;
            case Font.ITALIC:
                currentFontStyleText="Italic";
                break;
            default://bold italic
                currentFontStyleText="Bold Italic";
                        break;
        }
        currentFontStyleField=new JTextField(currentFontStyleText);
        currentFontStyleField.setPreferredSize(new Dimension(125,25));
        currentFontStyleField.setEditable(false);
        fontstylePanel.add(currentFontStyleField);

        //display list of all font style available
        JPanel listOfFontStylePanel = new JPanel();

        listOfFontStylePanel.setLayout(new BoxLayout(listOfFontStylePanel,BoxLayout.Y_AXIS));
        listOfFontStylePanel.setBackground(Color.WHITE);

        //list of font styles
        JLabel plainStyle=new JLabel("Plain");
        plainStyle.setFont(new Font("Dialogue",Font.PLAIN,12));
        plainStyle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //update the current style field
                currentFontStyleField.setText(plainStyle.getText());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                plainStyle.setOpaque(true);
                plainStyle.setBackground(Color.BLUE);
                plainStyle.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                plainStyle.setBackground(null);
                plainStyle.setForeground(null);
            }
        });
        listOfFontStylePanel.add(plainStyle);

        JLabel boldStyle=new JLabel("Bold");
        boldStyle.setFont(new Font("Dialogue",Font.BOLD,12));
        boldStyle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //update the current style field
                currentFontStyleField.setText(boldStyle.getText());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                boldStyle.setOpaque(true);
                boldStyle.setBackground(Color.BLUE);
                boldStyle.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boldStyle.setBackground(null);
                boldStyle.setForeground(null);
            }
        });

        listOfFontStylePanel.add(boldStyle);

        JLabel italicStyle=new JLabel("Italic");
        italicStyle.setFont(new Font("Dialogue",Font.ITALIC,12));
        italicStyle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //update the current style field
                currentFontStyleField.setText(italicStyle.getText());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                italicStyle.setOpaque(true);
                italicStyle.setBackground(Color.BLUE);
                italicStyle.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                italicStyle.setBackground(null);
                italicStyle.setForeground(null);
            }
        });
        listOfFontStylePanel.add(italicStyle);

        JLabel boldItalicStyle=new JLabel("Bold Italic");
        boldItalicStyle.setFont(new Font("Dialogue",Font.BOLD | Font.ITALIC,12));
        boldItalicStyle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //update the current style field
                currentFontStyleField.setText(boldItalicStyle.getText());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                boldItalicStyle.setOpaque(true);
                boldItalicStyle.setBackground(Color.BLUE);
                boldItalicStyle.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boldItalicStyle.setBackground(null);
                boldItalicStyle.setForeground(null);
            }
        });
        listOfFontStylePanel.add(boldItalicStyle);

        JScrollPane scrollPane=new JScrollPane(listOfFontStylePanel);
        scrollPane.setPreferredSize(new Dimension(125,125));
        fontstylePanel.add(scrollPane);


        add(fontstylePanel);
    }
    private void addFontSizeChooser(){
        JLabel fontsizelabel=new JLabel("Font Size: ");
        fontsizelabel.setBounds(275,5,125,10);
        add(fontsizelabel);

        //display the curret font size and list of font sizes to choose from
        JPanel fontSizePanel=new JPanel();
        fontSizePanel.setBounds(275,15,125,160);

         currentSizeField=new JTextField(
                Integer.toString(source.getTextArea().getFont().getSize())
        );
        currentSizeField.setPreferredSize(new Dimension(125,25));
        currentSizeField.setEditable(false);
        fontSizePanel.add(currentSizeField);

        //create list of font sizes to choose from
        JPanel listOfFontsizePanel=new JPanel();
        listOfFontsizePanel.setLayout(new BoxLayout(listOfFontsizePanel,BoxLayout.Y_AXIS));
        listOfFontsizePanel.setBackground(Color.WHITE);

        //list of available font sizes will be from 8 -> 72 with increaments of 2
        for (int i=8;i<=72;i+=2){

            JLabel fontSizeValueLable=new JLabel(Integer.toString(i));
            fontSizeValueLable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    //update current font size field
                    currentSizeField.setText(fontSizeValueLable.getText());
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                   //add highlights
                    fontSizeValueLable.setOpaque(true);
                    fontSizeValueLable.setBackground(Color.BLUE);
                    fontSizeValueLable.setForeground(Color.WHITE);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    //reset highlits
                    fontSizeValueLable.setBackground(null);
                    fontSizeValueLable.setForeground(null);
                }
            });
            listOfFontsizePanel.add(fontSizeValueLable);
        }
        JScrollPane scrollPane=new JScrollPane(listOfFontsizePanel);
        scrollPane.setPreferredSize(new Dimension(125,125));
        fontSizePanel.add(scrollPane);

        add(fontSizePanel);

    }
    private void addFontColourChooser(){
        //display to the user the current colour of the text
        currentColorBox=new JPanel();
        currentColorBox.setBounds(175,200,23,23);
        currentColorBox.setBackground(source.getTextArea().getForeground());
        currentColorBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(currentColorBox);

        JButton choosecolorButton= new JButton("Choose Colour: ");
        choosecolorButton.setBounds(10,200,150,23);
        choosecolorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color c=JColorChooser.showDialog(null,"Select a Colour",Color.BLACK);

                //update colorto selected colour
                currentColorBox.setBackground(c);
            }
        });
        add(choosecolorButton);
    }
}
