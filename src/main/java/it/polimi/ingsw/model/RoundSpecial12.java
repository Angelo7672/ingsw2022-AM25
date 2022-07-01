package it.polimi.ingsw.model;

/**
 * Round strategy used by special 1
 */
public class RoundSpecial12 extends RoundStrategy{
    Special12 special;

    public RoundSpecial12(int numberOfPlayer, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, QueueManager queueManager, Bag bag){
        super(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
        special = new Special12();
    }

    /**
     * It checks if effect could be used, then use it.
     * @param color is the student's color.
     * @return true if effect is used.
     */
    @Override
    public boolean effect(int color){
        if(color <5 && color > -1 ) {
            for (int i = 0; i < numberOfPlayer; i++) {
                if (playerManager.getStudentTable(i, color) >= 3) {
                    for (int j = 0; j < 3; j++)
                        playerManager.removeStudentTable(i, color);
                    bag.fillBag(color, 3);
                } else {
                    bag.fillBag(color, playerManager.getStudentTable(i, color));
                    while (playerManager.getStudentTable(i, color) != 0) playerManager.removeStudentTable(i, color);
                }
            }
            return true;
        }else return false;
    }

    @Override
    public void setCost(int cost){ special.setCost(cost); }
    @Override
    public int getCost(){ return special.getCost(); }
    @Override
    public void increaseCost(){ special.increaseCost(); }
    private static class Special12 extends Special {
        public Special12(){
            super(3);
        }
    }
}