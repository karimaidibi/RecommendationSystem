package recommender_system;
import java.util.*;


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
        int profileDimension = 3; // 3 profiles differents pour chaque user
        float maxNote = 5.0f;
        Random r = new Random();
        // creating users profiles
        for (int i = 0 ; i < nbUsers ; i++){
            ArrayList<Float> currentUserProfile = new ArrayList<Float>();

            for(int j = 0 ; j < profileDimension ; j++) {
                currentUserProfile.add((float) (r.nextFloat()*maxNote)); // next float un random float entre 0.0 et 1.0
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
                        // note du kième dimension du ième userProfile  * note du kième dimension du ième itemProfile

                    }
                    currentUserNote.add(note/((float)(maxNote*profileDimension)));
                } else {
                    currentUserNote.add(-1.0f);
                }

            }
            users.add(currentUserNote);
        }

		for (int i = 0 ; i < this.nbUsers; i++){

			for (int j = 0; j < this.nbItems ; j++){
				System.out.print(users.get(i).get(j)+ " ");
			}
			System.out.println();
		}
    }

    public static float cosineSimilarity(List<Float> vectorA, List<Float> vectorB) {
        float dotProduct = 0.0f;
        float normA = 0.0f;
        float normB = 0.0f;
        for (int i = 0; i < vectorA.size(); i++) {
            dotProduct += vectorA.get(i) * vectorB.get(i);
            normA += Math.pow(vectorA.get(i), 2);
            normB += Math.pow(vectorB.get(i), 2);
        }
        return (float) dotProduct /(float) (Math.sqrt(normA) * Math.sqrt(normB));
    }

    public Float[][] calculateSimilarity(){
        Float SimilarityMatrix[][] = new Float[users.size()][users.size()];
        for(int i = 0; i<users.size(); i++){
            for(int j = 0; j<users.size(); j++){
                Float cosinus = cosineSimilarity(users.get(i), users.get(j));
                SimilarityMatrix[i][j] = cosinus;
            }
        }
        return SimilarityMatrix;
    }

    public Float[][] calculerNotesManquantes(){
        // La matrice de similarité
        Float[][] CosMatrix = this.calculateSimilarity();
        // La matrice finale à renvoyer => contient les notes calculés
        Float MissingMatrix[][] = new Float[nbUsers][nbItems];
        float NoteNulle = -1.0f;
        // on itère sur tout les utilisateurs pour trouver les notes manquantes
        for(int i=0; i<nbUsers;i++){
            for(int j=0; j<nbItems; j++){
                // On reset les sommes de notes pondérés et le nb de notes prises pour le calcul
                Float sommeNotesPond = 0.0f;
                int notesPrises = 0;
                // Si la note est déjà renseignée, on la copie dans le tableau final
                if(users.get(i).get(j) != NoteNulle) {
                    MissingMatrix[i][j] = users.get(i).get(j);
                }
                // Si on trouve une note non calculée, on essaye de la calculer
                else{
                    // Pour chaque utilisateur de la colonne j
                    for(int k=0; k<nbUsers;k++){
                        // AUtre que l'utilisateur actuel (celui ou on veux trouver la note)
                        if(k != i){
                            // Si sa note n'est pas nulle et sa similarité avec l'utilsateur n'est pas négative
                            if(users.get(k).get(j) != NoteNulle && CosMatrix[k][j] > 0.0f) {
                                notesPrises++;
                                sommeNotesPond = (users.get(k).get(j))*(CosMatrix[k][j]);
                            }
                        }
                    }
                    // Quand la boucle est terminée, on calcule la note théorique pondérée
                    // et on la stocke dans la matrice finale
                    Float noteFinale = (float) sommeNotesPond/(float) notesPrises;
                    MissingMatrix[i][j] = noteFinale;
                }
            }
        }
        return MissingMatrix;
    }

    public static void main (String[] args){
        RecommenderSystem recSys = new RecommenderSystem();

        recSys.createFakeData(20, 10, 0.21f);

        System.out.println("---------------- COSINUS----------------");
        Float[][] CosMatrix = recSys.calculateSimilarity();
        for(int i = 0; i < CosMatrix.length; i++){
            System.out.println(Arrays.toString(CosMatrix[i]));
        }

        System.out.println("---------------- MATRICE FINALE----------------");
        Float[][] matriceFinale = recSys.calculerNotesManquantes();
        for(int i = 0; i < matriceFinale.length; i++){
            System.out.println(Arrays.toString(matriceFinale[i]));
        }

    }
}
