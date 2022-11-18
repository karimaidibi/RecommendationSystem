package recommender_system;
import java.util.ArrayList;
import java.util.Random;


/* Creating User Items Incomplete Matrix */
/* Les eleves doivent rajouter des méthodes pour faire le TP - à importer */

public class RecommenderSystem {
	private ArrayList<ArrayList<Float>>users = new ArrayList<ArrayList<Float>>();
	private int nbUsers = 0;
	private int nbItems = 0;
	
	public void createFakeData(int nbUsers, int nbItems, float sparsity) {
		this.nbItems = nbItems;
		this.nbUsers = nbUsers;
		ArrayList<ArrayList<Float>> userProfiles = new ArrayList<ArrayList<Float>>();
		ArrayList<ArrayList<Float>> itemsProfiles = new ArrayList<ArrayList<Float>>();
		int profileDimension = 3;
		float maxNote = 5.0f;
		Random r = new Random();
		// creating users profiles
		for (int i = 0 ; i < nbUsers ; i++){
			ArrayList<Float> currentUserProfile = new ArrayList<Float>();
			
			for(int j = 0 ; j < profileDimension ; j++) {
				currentUserProfile.add((float) (r.nextFloat()*maxNote));
			}
			userProfiles.add(currentUserProfile);
		}
		// creating items profiles
		for (int i = 0 ; i < nbItems ; i++){
			ArrayList<Float> currenItemProfile = new ArrayList<Float>();
			
			for(int j = 0 ; j < profileDimension ; j++) {
				currenItemProfile.add((float) (r.nextFloat()*maxNote));
			}
			itemsProfiles.add(currenItemProfile);
		}
		
		//generating rates from users and items profiles
		for (int i = 0 ; i < nbUsers ; i++) {
			ArrayList<Float> currentUserNote = new ArrayList<Float>();
			for (int j = 0 ; j < nbItems ; j++) {
				float newNote = r.nextFloat();
				if (newNote < sparsity) {
					
					float note = 0.0f;
					for (int k = 0 ; k < profileDimension ; k++) {
						note += userProfiles.get(i).get(k)*itemsProfiles.get(j).get(k);
						
					}
					currentUserNote.add(note/((float)(maxNote*profileDimension)));
				} else {
					currentUserNote.add(-1.0f);
				}
				
			}
			users.add(currentUserNote);
		}
		
		/*for (int i = 0 ; i < this.nbUsers; i++){
			
			for (int j = 0; j < this.nbItems ; j++){
				System.out.print(users.get(i).get(j)+ " ");
			}
			System.out.println();
		}*/
	}
	
	public static void main (String[] args){
		RecommenderSystem recSys = new RecommenderSystem();
		
		recSys.createFakeData(20000, 20000, 0.1f);
   }
}