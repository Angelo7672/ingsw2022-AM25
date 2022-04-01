package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.Random;

public class IslandsManager {
    private ArrayList<Island> islands;
    private Island i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12;
    private int motherPos;
    private Random rand;

    private class Island {

        private int[] students = {0,0,0,0,0};
        private int towerValue;
        private Team towerTeam; //-1=no one 0=white 1=black 2=grey

        Island(){
            towerValue=1;
            towerTeam = Team.NOONE;
        }

        public int getTowerTeam(){
            return towerTeam.getTeam();
        }
        public int getTowerValue(){
            return towerValue;
        }
        public void setTowerTeam(int team){
            towerTeam.setTeam(team);
        }
        public void incTowerValue(){
            towerValue++;
        }
        public int getNumStudents(int color){
            return students[color];
        }
        public void setNumStudents(int color, int nStudents){
            students[color] = nStudents;
        }
        public void incStudents(int color){
            students[color] += 1;
        }
    }

    IslandsManager() {
        i1 = new Island(); i2 = new Island(); i3 = new Island();
        i4 = new Island(); i5 = new Island(); i6 = new Island();
        i7 = new Island(); i8 = new Island(); i9 = new Island();
        i10 = new Island(); i11 = new Island(); i12 = new Island();
        islands = new ArrayList<>();
        islands.add(i1); islands.add(i2); islands.add(i3);
        islands.add(i4); islands.add(i5); islands.add(i6);
        islands.add(i7); islands.add(i8); islands.add(i9);
        islands.add(i10); islands.add(i11); islands.add(i12);
        rand=new Random();
        motherPos = rand.nextInt(12);
    }

    /*setup nel caso si passasse un array
    public void setup(ArrayList<Integer> color){
        for(int i=0; i<12; i++){
            if(i!=motherPos && i!=motherPos+6){
                islands.get(i).incStudents(color.get(0));
                color.remove(0);
            }
        }
    }*/

    //se si passa puntatore di posizione (tanto viene creato un ciclo per chiamare 10 volte il metodo per inserire gli studenti quindi si passa la variabile)
    //senza verificare se va bene per pos madre. Nel caso si verifica basta togliere l'if e si può usare sempre durante la partita (come setStudent)
    public void setup(int color, int island){
        if(island!=motherPos && island!=motherPos+6){
            islands.get(island).incStudents(color);
        }
    }

    public void moveMotherNature(int steps) {
        motherPos += steps;
        if (motherPos > 11) motherPos = motherPos - 12;
    }

    public void  incStudent(int island, int color){
        islands.get(island).incStudents(color);
    }

    //check che vanno fatti insieme dopo spostamento studenti in isole. private o public?
    //variabile pos utile solo nel caso in cui si usano gli specials perchè senza si usa motherPos sempre
    private void conquestIsland(ArrayList<Integer> prof, int pos) {
        int playerInfluence = highestInfluenceTeam(prof, pos); //int che contiene il player con influenza maggiore sull'isola selezionata
        if(playerInfluence != islands.get(pos).getTowerTeam() && playerInfluence != -1) { //se l'influenza è cambiata e se è != -1
            towerChange(playerInfluence, pos, players); //metodo da sistemare.
            checkAdjacentIslands(pos);
            checkVictory(); //lasciato qua non serve a niente ma è per intendere che ha senso farlo solo in caso di conquiste o meglio se solo dopo unione di isole
        }
    }

    //per controllare chi ha l'influenza maggiore bisogna avere accesso alla scuola di ogni studente.
    //se creiamo un metodo in player manager che crea un array di interi contenente il player che possiede il prof ci risparmiamo l'accesso
    //nel caso di 4 giocatori nell'array dei prof entrambi i giocatori white saranno messi con dicitura 0, entrambi i black con 1.
    //Il metodo ritorna chi ha l'influenza maggiore, -1 se nessuno
    private int highestInfluenceTeam(ArrayList<Integer> prof, int pos) {
        int inflP1 = 0, inflP2 = 0, inflP3 = 0;
        for(int i=0; i<5; i++) {
            if(islands.get(pos).getNumStudents(i)>0)
                //aggiunge a chi possiede il prof di quel colore il numero di studenti
                //se aggungiamo una variabile influence ad ogni player ci evidiamo lo switch
                switch (prof.get(i)){
                    case(0): inflP1+=islands.get(pos).getNumStudents(i); break;
                    case(1): inflP2+=islands.get(pos).getNumStudents(i); break;
                    case(2): inflP3+=islands.get(pos).getNumStudents(i); break;
                }
        }
        //non so se è meglio metterli dentro ad un metodo findMax questi if
        if (inflP1>inflP2 && inflP1>inflP3) return 0;
        if (inflP2>inflP1 && inflP2>inflP3) return 1;
        if (inflP3>inflP2 && inflP3>inflP1) return 2;
        return -1;
    }


    //bisogna farlo in manager perchè serve l'accesso alla scuola dei players
    private void towerChange(int player, int pos, ArrayList<Player> players) {
        //se c'era gia una torre sull'isola
        if (islands.get(pos).getTowerTeam() != -1) {
            /*metodo da scrivere a seconda di come viene scritto player e scuola. riporta la torre (o le torri) nella scuola
                es: player.getSchool.setNumTowers(numTowers+towerValue).
             */
        }
        islands.get(pos).setTowerTeam(player); //setto il player che ha conquistato l'isola

        //metodo da scrivere a seconda di come viene scritto player e scuola.
        //setta il numero di torri nella scuola di chi ha conquistato = numero torri-towerValue

    }

    private void checkAdjacentIslands(int pos) {
        //se quella a sx ha torre uguale
        if (islands.get(pos).getTowerTeam() == islands.get(pos - 1).getTowerTeam()) {
            //sommo studenti in isola a dx
            for (int i = 0; i < 5; i++) {
                islands.get(pos).setNumStudents(i, islands.get(pos).getNumStudents(i) + islands.get(pos-1).getNumStudents(i));
            }
            islands.get(pos).incTowerValue(); //incremento valore torre
            islands.remove(pos-1); //elimino l'isola a sx
            if(motherPos == pos-1) motherPos = pos; //sposto madre, nel caso ci fosse, dall'isola eliminata
        }
        //se quella a dx ha torre uguale
        if (islands.get(pos).getTowerTeam() == islands.get(pos + 1).getTowerTeam()) {
            for (int i = 0; i < 5; i++) {
                islands.get(pos).setNumStudents(i, islands.get(pos + 1).getNumStudents(i) + islands.get(pos).getNumStudents(i));
            }
            islands.get(pos).incTowerValue();
            islands.remove(pos + 1);
            if(motherPos == pos+1) motherPos = pos; //sposto madre, nel caso ci fosse, dall'isola eliminata
        }
    }

    //true se le isole rimangono 3
    private boolean checkVictory(){
        if(islands.size()==3) return true;
        return false;
    }

    public int getMotherPos(){
        return motherPos;
    }


    //avevo scritto un print mentre controllavo se fosse giusto, lo lascio se magari serve poi
    public void printIslands(int pos) {
        System.out.println();
        System.out.print("isola numero " + pos);
        System.out.print(" Verde: " + islands.get(pos).getNumStudents(0) + " Rosso: " + islands.get(pos).getNumStudents(1) +
                " Giallo: " + islands.get(pos).getNumStudents(2) + " Rosa: " + islands.get(pos).getNumStudents(3) +
                " Azzurro: " + islands.get(pos).getNumStudents(4));
        switch (islands.get(pos).getTowerTeam()) {
            case (-1):
                System.out.print(" Non ci sono torri");
                break;
            case (0):
                System.out.print(" torri banchi:  " + islands.get(pos).getTowerValue());
                break;
            case (1):
                System.out.print(" torri nere:  " + islands.get(pos).getTowerValue());
                break;
            case (2):
                System.out.print(" torri grige:  " + islands.get(pos).getTowerValue());
                break;
        }
    }


}

