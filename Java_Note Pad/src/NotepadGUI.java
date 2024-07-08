import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.io.*;


public class NotepadGUI extends JFrame {
    //file explorer

    private JFileChooser fileChooser;
    private JTextArea textArea;
    public JTextArea getTextArea(){return textArea;}
    private File currentFile;

    //swing's built in library to manage undo and redo functionality
    private UndoManager undoManager;

    public NotepadGUI(){
        super("Notepad");
        setSize(400,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("src/assets"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files","txt"));

        undoManager = new UndoManager();


        addGuiComponents();



    }
    private void addGuiComponents(){
        addToolbar();

        //area to type text into
         textArea=new JTextArea();

         textArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
             @Override
             public void undoableEditHappened(UndoableEditEvent e) {
                 undoManager.addEdit(e.getEdit());
             }
         });

         JScrollPane scrollPane=new JScrollPane(textArea);
        add(scrollPane,BorderLayout.CENTER);
    }
    private void addToolbar(){
        JToolBar toolbar= new JToolBar();
        toolbar.setFloatable(false);
        //menu bar
        JMenuBar menubar= new JMenuBar();
        toolbar.add(menubar);
        //add menus
        menubar.add(addFileMenu());
        menubar.add(addEditMenu());
        menubar.add(addFormatMenu());
        menubar.add(addViewMenu());

        add(toolbar, BorderLayout.NORTH);
    }
    private JMenu addFileMenu(){
        JMenu filemenu= new JMenu("File");


        //"new" menu functionality
        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //rest title header
                setTitle("Notepad");
                //rset textarea
                textArea.setText("");

                //reset current file
                currentFile = null;

            }
        });
        filemenu.add(newMenuItem);


        //"open" menu functionality
        JMenuItem openMenuItem= new JMenuItem("Open");
        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = fileChooser.showOpenDialog(NotepadGUI.this);
                if (result != JFileChooser.APPROVE_OPTION) return;

               try{
                   //reset notepad
                   newMenuItem.doClick();

                   //get the Selected file
                   File selectedfile= fileChooser.getSelectedFile();

                   //update current file
                   currentFile = selectedfile;

                   //Update title Header
                   setTitle(selectedfile.getName());

                   //read the file
                   FileReader filereader =new FileReader(selectedfile);
                   BufferedReader bufferedReader=new BufferedReader(filereader);

                   //store the text
                   StringBuilder fileText = new StringBuilder();
                   String readText;
                   while ((readText = bufferedReader.readLine()) !=null){
                       fileText.append(readText + "\n");

                   }

                   //update text area GUI
                   textArea.setText(fileText.toString());
               }catch (Exception e1){

               }
            }
        });
        filemenu.add(openMenuItem);

        //"saveAS" menu functionality
        JMenuItem saveASMenuItem=new JMenuItem("SaveAs");
        filemenu.add(saveASMenuItem);
        saveASMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //open save dialogue

                int result = fileChooser.showSaveDialog(NotepadGUI.this);

                //continue to execute code only if the user pressed the sve butteon
                if (result != JFileChooser.APPROVE_OPTION) return;

               try{
                   File selectedfile = fileChooser.getSelectedFile();
                   //we will append .txt to the file if it does not have the txt extension yet

                   String filename=selectedfile.getName();
                   if (!filename.substring(filename.length() - 4).equalsIgnoreCase(".txt")){
                       selectedfile = new File(selectedfile.getAbsoluteFile() + ".txt");
                   }
                   //create new file

                   selectedfile.createNewFile();

                   //now we ill write the useers txt into the file that we just created

                   FileWriter fileWriter=new FileWriter(selectedfile);
                   BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
                   bufferedWriter.write(textArea.getText());
                   bufferedWriter.close();
                   fileWriter.close();

                   //update the title header of GUI to the save text file
                   setTitle(filename);

                   //update current file
                   currentFile=selectedfile;

                   //show display dialogue
                   JOptionPane.showMessageDialog(NotepadGUI.this,"Saved File");
               }
               catch (Exception e1){
                   e1.printStackTrace();
               }

            }
        });

        //"Save" menu functionality
        JMenuItem saveMenuItem=new JMenuItem("Save");
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //if the current file is null then we have to perform save as functionality
                if (currentFile==null)saveASMenuItem.doClick();

                //if the user chooes to canceel saving the file ths means that current file will still
                //be null,then we wan to prevent executing the rest of the code
                if(currentFile==null)return;

                try{
                    //write current file
                    FileWriter fileWriter= new FileWriter(currentFile);
                    BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
                    bufferedWriter.write(textArea.getText());
                    bufferedWriter.close();
                    fileWriter.close();

                }catch (Exception e1){
                    e1.printStackTrace();

                }

            }
        });
        filemenu.add(saveMenuItem);

        //"Exit" menu functionality
        JMenuItem exitMenuItem= new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //dispose the Notepad GUI
                NotepadGUI.this.dispose();
            }
        });
        filemenu.add(exitMenuItem);

        return filemenu;
    }
    private JMenu addEditMenu(){
        JMenu editMenu = new JMenu("Edit");

        //undo edit menu functionalities
        JMenuItem undoMenuItem= new JMenuItem("undo");
        undoMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //it means thet if there are any edits that we can undo,then we can undo them
                if (undoManager.canUndo()){
                    undoManager.undo();
                }
            }
        });
        editMenu.add(undoMenuItem);

        //redo eedit menu functionalities
        JMenuItem redoMenuItem= new JMenuItem("redo");
        redoMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //it means if there is an edit that we can redo then we redo it
                if (undoManager.canRedo()){
                    undoManager.redo();
                }
            }
        });
        editMenu.add(redoMenuItem);

        return editMenu;
    }
    private JMenu addFormatMenu(){
        JMenu formatmenu= new JMenu("Format");

        //wrap word functionality
        JCheckBoxMenuItem wordWrapMenuItem=new JCheckBoxMenuItem("Word Wrap");
       wordWrapMenuItem.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               boolean isChecked= wordWrapMenuItem.getState();
               if (isChecked){
                   //wrap words
                   textArea.setLineWrap(true);
                   textArea.setWrapStyleWord(true);
               }
               else {
                   //unwrap words
                   textArea.setLineWrap(false);
                   textArea.setWrapStyleWord(false);
               }

           }
       });
       formatmenu.add(wordWrapMenuItem);

       //align Text
        JMenu alignTextmenu=new JMenu("Align Text");

        //ALIGN TEXT TO THE LEFT
        JMenuItem alignTextLeftMenu=new JMenuItem("Left");
        alignTextLeftMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            }
        });
        alignTextmenu.add(alignTextLeftMenu);

        //ALIGN TEXT TO THE RIGHT
        JMenuItem alignTextRightMenu=new JMenuItem("Right");
        alignTextRightMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            }
        });
        alignTextmenu.add(alignTextRightMenu);
        formatmenu.add(alignTextmenu);

        //font Format
        JMenuItem fontMenuItem=new JMenuItem("Font...");
        fontMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //launch font menu
                new FontMenu(NotepadGUI.this).setVisible(true);
            }
        });
        formatmenu.add(fontMenuItem);

        return formatmenu;

    }
    private JMenu addViewMenu(){
        JMenu viewMenu=new JMenu("View");

        JMenu zoomMenu=new JMenu("Zoom");

        //Zoomin functionality
        JMenuItem zoomInMenuItem= new JMenuItem("Zoom In");
        zoomInMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Font currentFont=textArea.getFont();
                textArea.setFont(new Font(
                        currentFont.getName(),currentFont.getStyle(),currentFont.getSize()+1
                ));
            }
        });
        zoomMenu.add(zoomInMenuItem);

        //zoom out functiobality
        JMenuItem zoomOutmenuItem=new JMenuItem("Zoom Out");
        zoomOutmenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Font currentFont=textArea.getFont();
                textArea.setFont(new Font(
                        currentFont.getName(),currentFont.getStyle(),currentFont.getSize()-1
                ));

            }
        });
        zoomMenu.add(zoomOutmenuItem);

        //restore default zoom
        JMenuItem zoomRestoreMenuItem=new JMenuItem("Restore Default Zoom");
        zoomRestoreMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Font currentFont=textArea.getFont();
                textArea.setFont(new Font(
                        currentFont.getName(),currentFont.getStyle(),12
                ));


            }
        });
        zoomMenu.add(zoomRestoreMenuItem);

        viewMenu.add(zoomMenu);
        return viewMenu;
    }
}
