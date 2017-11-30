/* 
 * Centro Universitário Senac - Campus Santo Amaro
 * Curso: Tecnologia em Jogos Digitais
 * Turma: 4 Semestre - 2º Período 2013
 * Aluno: Marcos Antônio Marcon
 * Matéria: Jogos em Rede
 * Professor: Cesar Alison Monteiro Paixão
 */

package tictactoe;

import javax.swing.JFrame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class TicTacToe extends javax.swing.JFrame implements ActionListener{
    private GridLayout layout;
    private static SocketsTCP socket;
    private JButton[] jButton;
    private int[] buttonWasClicked;
    private boolean gameEnd;
    private boolean isYourTurn;
    private int turnCounter;
    private ImageIcon iconV, iconX, iconTitle;
    private static SimpleThread thread;
    
    //Construtor da classe que carrega todos os componentes do jogo
    //Janela, botões, imagens, socket.
    public TicTacToe() {
        socket = new SocketsTCP();
        
        iconTitle = new ImageIcon("src/images/t.png");
        this.setIconImage(iconTitle.getImage());
        
        layout = new GridLayout(3, 3); 
        getContentPane().setLayout(layout);
        
        jButton = new JButton[9];
        buttonWasClicked = new int[9];
        
        iconV = new ImageIcon("src/images/v.png");
        iconX = new ImageIcon("src/images/x.png");
        
        gameEnd = false;
        turnCounter = 1;
	
        //Inicializando os botões
        for(int i=0; i < 9; i++){
            buttonWasClicked[i] = -1;
            jButton[i] = new javax.swing.JButton();
            jButton[i].setText("");
            jButton[i].addActionListener(this);
            add(jButton[i], i);
        }
    }

    //Método que define o estado inicial de jogo
    public void init(){
        if(!socket.isServer()){
            this.setTitle("TicTacToe - Player2 -- [YOUR TURN]");
            isYourTurn = true;
        }
        else{
            this.setTitle("TicTacToe - Player1<Host>");
            isYourTurn = false;
        }	
    }
    //Método que confere as movimentações dos jogadores e atualiza o estado de jogo
    public void getMove(int i){
        if(!gameEnd){
            turnCounter++;
            if(isYourTurn){
                jButton[i].setIcon(iconV);
                isYourTurn = false;
                if(socket.isServer()){
                    buttonWasClicked[i] = 1;
                    this.setTitle("TicTacToe - Player1<Host>");
                }
                else{
                    buttonWasClicked[i] = 0;
                    this.setTitle("TicTacToe - Player2");
                }
            }
            else{
                jButton[i].setIcon(iconX);
                isYourTurn = true;
                if(socket.isServer()){
                    buttonWasClicked[i] = 0;
                    this.setTitle("TicTacToe - Player1<Host> -- [YOUR TURN]");
                }
                else{
                    buttonWasClicked[i] = 1;
                    this.setTitle("TicTacToe - Player2 -- [YOUR TURN]");
                }
            }
            getGameState();
        }
    }
    //Método principal que inicializa a tela de jogo o socket e a thread
    public static void main(String[] args) {
            TicTacToe window = new TicTacToe();
	    window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	    window.setSize( 400, 400 );
            window.setVisible( true );
            window.init();
            thread = new SimpleThread(window, window.socket);
            thread.start();
    }
    //Método que confere o turno
    public boolean getIsYoutTurn(){
        return isYourTurn;
    }

    //Método que captura a ação do jogador
    @Override
    public void actionPerformed(ActionEvent ae) {
        if(!gameEnd && isYourTurn){
            for(int i=0; i<9; i++){
                if(buttonWasClicked[i] == -1 && ae.getSource() == jButton[i]){
                    getMove(i);
                    socket.sendInt(i);
                }
            }
        }
    }
    //Método para definir o  turno
    public void setYourTurn(boolean state){
        isYourTurn = state;
    }
    //Método que aleta o jogador se o jogo acabou
    public void getGameState(){
        switch(check()){
            case 0:
                gameEnd = true;
                JOptionPane.showMessageDialog(null, "Fim de jogo!\nJogador 2 Venceu!", null, JOptionPane.PLAIN_MESSAGE , null);
                break;
            case 1:
                gameEnd = true;
                JOptionPane.showMessageDialog(null, "Fim de jogo!\nJogador 1 Venceu!", null, JOptionPane.PLAIN_MESSAGE , null);
                break;
            case 2:
                gameEnd = true;
                JOptionPane.showMessageDialog(null, "Fim de jogo!\nEmpate(Velha)!", null, JOptionPane.PLAIN_MESSAGE , null);
                break;
        }
    }
    //Método que confere se o jogo acabou
    public int check(){
        //Checando Linhas Horizontais
        for(int i=0; i<=6; i+=3){
            if (buttonWasClicked[i] == buttonWasClicked[i+1] && buttonWasClicked[i] == buttonWasClicked[i+2]) {
                switch(buttonWasClicked[i]){
                    case 0: return 0;
                    case 1: return 1;
                }
            }
        }
        //Checando Linhas Verticais
        for(int i=0; i<=2; i++){
            if (buttonWasClicked[i] == buttonWasClicked[i+3] && buttonWasClicked[i] == buttonWasClicked[i+6]) {
                switch(buttonWasClicked[i]){
                    case 0: return 0;
                    case 1: return 1;
                }
            }
        }
        //Checando Diagonal Esquerda
        if (buttonWasClicked[0] == buttonWasClicked[4] && buttonWasClicked[0] == buttonWasClicked[8]) {
            switch(buttonWasClicked[0]){
                case 0: return 0;
                case 1: return 1;
            }
        }
        //Checando Diagonal Direita
        if (buttonWasClicked[2] == buttonWasClicked[4] && buttonWasClicked[2] == buttonWasClicked[6]) {
            switch(buttonWasClicked[2]){
                case 0: return 0;
                case 1: return 1;
            }
        }
        //Checando se deu velha
        if(turnCounter > 9){
            return 2;
        }
        //Dando continuidade ao jogo
        return -1;
    }
}