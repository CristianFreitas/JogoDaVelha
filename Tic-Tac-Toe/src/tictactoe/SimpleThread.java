package tictactoe;

public class SimpleThread extends Thread{

    private TicTacToe janela;
    private SocketsTCP socket;
    //construtor da thread onde são passados como parâmetros a janela e o socket
    public SimpleThread(TicTacToe window, SocketsTCP skt){
        super();
        janela = window;
        socket = skt;
    }
    //Método em loop da thread que recebe a informação da jogada do outro player
    @Override
    public void run(){
        try{
            while(true){
                janela.getMove(socket.recvInt());
            }
        }catch(Exception e){
            this.interrupt();
        }
    }
}
