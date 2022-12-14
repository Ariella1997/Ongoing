import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.Color ; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener ;
import java.awt.GridLayout;

public class EvidenceScreen implements KeyListener,ActionListener{

    // Button array will make us be able to use forloops for various code in this class
    JButton[] buttonArray = new JButton[6] ; 
    // Panels that will hold the evidenceScreen. ImagePanel holds buttons which hold icons of the evidence. TextPanel will hold a JTextArea that will have the information about selected evidence. 
    JPanel evidenceImagePanel = new JPanel() ; 
    JPanel evidenceTextPanel = new JPanel() ; 
    // JLabel will hold images that will a full screen image version of icons. Greats for showing maps.
    JLabel EvidenceFullImageLabel = new JLabel() ; 
    // JTextArea will hold text with information on the evidence
    JTextArea evidenceTextArea = new JTextArea() ;  
    // Page Number will tell us which evidence page we are on. Incremented by the forwardbutton and decremented by the backbutton.
    int evidencePageNumber = 0 ; 
    // Buttons won't hold focus. This acts as a fake focus holder, so we know which evidence the user was talking about. 1 represents the leftmost icon. 
    int currentSelectedButton = 1 ; 
    // Game object that we can use to manipulate the game state from here.
    Game game ; 

    public EvidenceScreen(Game currentgame){
        // This will be the main way we extract and manipulate game data state whilst we are on the evidence Screen. 
        game = currentgame ; 

        // Buttons will hold images of the various evidences. 
        // backbutton and forward button will have arrows to tell show people what it's for.
        JButton backbutton = new JButton("<-") ;
        JButton button1 = new JButton() ; 
        JButton button2 = new JButton() ; 
        JButton button3 = new JButton() ; 
        JButton button4 = new JButton() ;
        JButton forwardbutton = new JButton( "->");
        // Action commands will be passed onto the actionperformed method, and the appropriate action will be selected there. See actionPerformed.
        // 1-4 means we have selected a button with an icon, so we need to select it, and view the appropriate text
        // 5 and 6 are for the forward and backbutton, so we need to change the page we are on. 
        backbutton.setActionCommand("5");
        button1.setActionCommand("1") ; 
        button2.setActionCommand("2") ; 
        button3.setActionCommand("3") ; 
        button4.setActionCommand("4") ; 
        forwardbutton.setActionCommand("6");
        // Jbuttons are put into an array. We can now reference the buttonArray, rather than the buttons itself.
        // Buttons are put in this specific order, so we can use a forloop.  
        buttonArray[0] = backbutton ; 
        buttonArray[1] = button1 ; 
        buttonArray[2] = button2 ; 
        buttonArray[3] = button3 ; 
        buttonArray[4] = button4 ; 
        buttonArray[5] = forwardbutton ; 

        // Adding ActionListners to all 6 buttons. (For actionPerformed method)
        for(int i = 0 ; i < buttonArray.length ; i++){
            buttonArray[i].addActionListener(this) ; 
        }

        // Setting the Panels as a GridLayout, and it's position and size. evidenceTextpanel will match the evidenceImagePanel's size, and the position will be underneath the evidenceImagePanel
        evidenceImagePanel.setLayout(new GridLayout(1 , buttonArray.length)) ; 
        evidenceImagePanel.setBounds(50, (int) game.getVisualScreen().getHeight()/2 , 600, 100);
        evidenceTextPanel.setBounds(evidenceImagePanel.getX() , evidenceImagePanel.getY() + evidenceImagePanel.getHeight() , evidenceImagePanel.getWidth() , evidenceImagePanel.getHeight()) ;

        // Adding the buttons to the evidenceImagePanel.
        for(int i = 0; i < buttonArray.length ; i++){
             evidenceImagePanel.add(buttonArray[i]) ; 
        }

        // Some JTextArea settings to make the JTextArea act like a JLabel.
        // JTextArea has LineWrap and WrapSyleWords to help us with making sure that the words fit onto the screen. (Not found in JLabel)
        // Warps mean we can write the text as long as necessary. 
        evidenceTextArea.setSize(evidenceTextPanel.getWidth(), evidenceTextPanel.getHeight());
        evidenceTextArea.setLineWrap(true);
        evidenceTextArea.setWrapStyleWord(true);
        evidenceTextArea.setOpaque(false);
        evidenceTextArea.setBorder(null) ; 
        evidenceTextArea.setEditable(false);
        evidenceTextArea.setFocusable(false);

        // Font to match with the buttons. (JTextArea has a different normal font than the other swing objects, weird, but ok.)
        evidenceTextArea.setFont(backbutton.getFont()) ; 

        // Adding the JTextArea to the TextPanel, so we can write the description of the evidence for the user to see.
        evidenceTextPanel.add(evidenceTextArea) ; 

        // method to update text, icons and enablement of buttons. See buttonUpdater() for details.
        buttonUpdater();

        // Adding the various objects to the visualLayer for us to see. 
        game.getVisualLayer().add(evidenceImagePanel , Integer.valueOf(3)) ;
        game.getVisualLayer().add(evidenceTextPanel , Integer.valueOf(3)) ;

        // Drawing so the components are visible. 
        game.getVisualLayer().validate();
        game.getVisualLayer().repaint();
        // Giving focus to the evidenceImagePanel and adding the KeyListener to the evidenceImagePanel
        evidenceImagePanel.requestFocusInWindow() ; 
        evidenceImagePanel.addKeyListener(this);

    }

    public void buttonUpdater() {
        // Clears whatever was in the buttons before the method was called. [0] is left for the backbutton, and [5] is left for the forwardbutton.
        // Text will be added to show that the various buttons are indeed empty
        for( int i =1 ; i <= 4  ; i++){
            buttonArray[i].setIcon(null);
            buttonArray[i].setText("Empty") ; 
        }
        // If there is evidece to fill the space, then the appropriate icon will be extracted.
        // Text that said a button is empty (Previous for loop) is now removed.
        // The min() means that we only fill the necessary amount of boxes for the evidence we have.
        for(int i = 1 ; i  <= Math.min(4 , game.getEvidenceImageList().size() - 4*evidencePageNumber ); i++ ){
            buttonArray[i].setText(null);
            buttonArray[i].setIcon(game.getEvidenceImageList().get(4*evidencePageNumber + i - 1)); 
        }
        // Disables the backbutton to Stops us from going into negative pages.
        buttonArray[0].setEnabled(evidencePageNumber > 0);
        // Enables any button that would have an icon in, and the forward button if there are more evidence to be shown. 
        for( int i =1 ; i <= 5  ; i++){
            buttonArray[i].setEnabled(4*evidencePageNumber+ i-1 < game.getEvidenceImageList().size()) ; 
        }
        // Text is updated to be the left most icon's description. 
        // currentSelectedButton is set to 1 to match the description.
        evidenceTextArea.setText(game.getEvidenceTextList().get(4*evidencePageNumber));
        currentSelectedButton = 1 ; 
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Extract the actioncommand from the button, converts the string into text, and holds it for us here. 
        currentSelectedButton = Integer.valueOf(e.getActionCommand()) ; 
        // Whenever a button is pushed, focus is given to the button. This makes sure that the focus returns to the evidenceImagePanel
        evidenceImagePanel.requestFocusInWindow() ; 
        // <=4 Suggests that the button pressed was one that has is enabled, and has an icon. This will simply get the description text for the associated icon.
        if( Integer.valueOf(e.getActionCommand()) <= 4 ){
            evidenceTextArea.setText(game.getEvidenceTextList().get(-1 + 4*evidencePageNumber + currentSelectedButton)) ; 
        }
        // The two remaining buttons are for when we have to move pages. The page number is incremented or decremented, and the buttons image, text and enability are updated. Backbutton had actioncommand 5, and forward button hadd actioncommand 6. -1^5 = -1 (Decrementing the pageNumber) and -1^6 = 1 (Incrementing the pageNumber). Hence the Math.pow method used. 
        // buttonUpdater is called, to update text,images, and enability. 
        else{
            evidencePageNumber = evidencePageNumber + (int) Math.pow(-1 , currentSelectedButton);
            buttonUpdater() ; 
        }
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Code for when the evidenceImagePanel has focus (i.e no FullImage of the evidence is presented)
        if(evidenceImagePanel.hasFocus()){
            switch(e.getKeyChar()){
                // Presents the full image of the evidence if it has one. Will do nothing otherwise. 
                case 'q' :
                    // Checks for existance of an image that we can use. (Image is placed using the "Add Evidence" from the evidenceUpdater method in the Game object)
                    if(game.getEvidenceFullImageList().get(4*evidencePageNumber + currentSelectedButton - 1) != null){
                        // Get Icon from the evidenceFullImageList, set it to the Label, give it bounds, add to the visualLayer(), above everything.
                        EvidenceFullImageLabel.setIcon(game.getEvidenceFullImageList().get(4*evidencePageNumber + currentSelectedButton - 1) ) ; 
                        EvidenceFullImageLabel.setBounds(0 , 0 , game.getVisualScreen().getWidth() , game.getVisualScreen().getHeight()) ; 
                        game.getVisualLayer().add(EvidenceFullImageLabel , Integer.valueOf(4)) ; 
                        // Adding focus to the Label, and KeyListener, so that we can close it later, but not close the evidenceScreen behind it. 
                        EvidenceFullImageLabel.requestFocusInWindow() ; 
                        EvidenceFullImageLabel.addKeyListener(this);
                        // Repainting to make the image visible. 
                        game.getVisualLayer().validate();
                        game.getVisualLayer().repaint();
                    }
                    break ;
                // Deletes the evidenceScreen, and returns focus to the gameScreen.
                case 'e' :
                    // Removes the current istance of evidencePanels.
                    game.getVisualLayer().remove(evidenceImagePanel) ; 
                    game.getVisualLayer().remove(evidenceTextPanel) ;
                    // Repaints 
                    game.getVisualLayer().validate() ;
                    game.getVisualLayer().repaint() ;
                    // Gives the focus back the gameScreen, so that we can move the text again with mousePressed / Spacebar
                    game.getGameScreen().requestFocusInWindow() ; 
                    break ;
                // Presents evidence in court.
                // Todo: Text won't correctly format once you press r and get the objection. 
                case 'r' :
                    if(game.getTextDisplay().getForeground() == Color.GREEN){ 
                        // If timer is running (The witness was still speaking), we stop the timer (Stopping new characters from being added) and reset the characterCounter to 0 (So when we press the mouse again, the game starts reading from the start of the sentence). 
                        if(game.getTimer().isRunning()){
                            game.getTimer().stop();
                            game.setCharacterCounter(0)  ; 
                        }
                        // Removes the current instance of evidencePanels.
                        game.getVisualLayer().remove(evidenceImagePanel) ;
                        game.getVisualLayer().remove(evidenceTextPanel) ;
                        // Adding an objection bubble, cause that is what the main character is going to bee saying.
                        game.getMessage().add(game.getMessageCounter() + 1 , "Objection") ; 
                        game.getMessage().add(game.getMessageCounter() + 1 , "Bubble") ;
                        // Using the imageUpdater to add the objection bubble to the screen 
                        game.imageUpdater();
                        // Removing the newly added command text. (In case we need to traverse this section again.)
                        game.getMessage().remove(game.getMessageCounter() - 2) ; 
                        game.getMessage().remove(game.getMessageCounter() - 2) ; 
                        game.setMessageCounter(game.getMessageCounter() - 2) ; 
                        // Adding a removebubble command. Decreementing the button as we've just added a new getMessage() so the command will be activated once we do a mousePress. (This remove bubble is an unatural remove bubble and will be removed by the imageUpdater() method when doing the command)
                        game.getMessage().add(game.getMessageCounter() , "Remove Bubble") ;
                        game.setMessageCounter(game.getMessageCounter() - 1 ) ; 
                        // Since we can't do a mousePress (gameScreen won't have focus until we leave this section of code), we are going to manually set the name and text to empty. 
                        game.getCharacterNameDisplay().setText("");
                        game.getTextDisplay().setText("") ; 
                        // Stops the music. Just style effects (Should only stop if the presented evidence was corrrect but that is for another time)
                        if(game.getMusicClip() != null){
                            game.getMusicClip().stop();
                            game.getMusicClip().flush();
                        }
                        // Giving focus back to the screen. 
                        game.getGameScreen().requestFocusInWindow() ; 
                    break; 
                //Todo: R present evidence button. See idea below. (Work in progress, for now, only adds objection bubble)
                // Will do a quick check for the correct answer
                // If wrong, then it will add some messages to the messageList, stating that the evidence presented is wrong
                // Need to add a lifepoint system that decrements when we get the wrong answer
                // Added messages will then be deleted, (so when we loop again, we do not read the same messages)
                // getMessageCounter() will be stored and we will be brought back to the testimony with that information.
                // If right, then it will set the getMessage() counter to to next digit after a repeat command (repeat command moves the getMessage() counter back)
                // Game should proceed from there. 
                }
            }
        }
        // Code for when the full image of the code is presented
        // Doesn't need the fullevidenceImage.hasFocus() part, but will add it for clarity. 
        else if(EvidenceFullImageLabel.hasFocus()){
            if(e.getKeyChar() == 'q'){
                // Remove the evidenceFullImageLabel, so we can see the evidenceScreen again.
                game.getVisualLayer().remove(EvidenceFullImageLabel) ; 
                // Gives the foucs back to the evidenceImagePanel
                evidenceImagePanel.requestFocusInWindow() ; 
                // Repainting the screen for us to see everything again. 
                game.getVisualLayer().validate();
                game.getVisualLayer().repaint();
                // Apparently, if we remove but not create a new JLabel, the JLabel would accumulate "null" items (do System.out.println(EvidenceFullImageLabel.getParents())). Overtime, this may cause an error to appear in the terminal (Although everything works as expected and Image did appear/disappear as requested) . The below code deletes the previous instance of the JLabel and prevents "null" items from accumulating, preventing the error from ever happening.
                // Programming do be like that sometimes.
                EvidenceFullImageLabel = new JLabel() ; 
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
        
    }

}
