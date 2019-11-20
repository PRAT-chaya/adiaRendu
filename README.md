- Alexy ROUSSEAU 21910036
- Brian SABATIER 21907858
- Axel DENOUAL 21600639

# Rendu du Fil Rouge du module d'Aide à la décision et Intelligence artificielle :

Le package examples contient quatres classes exécutables qui utilisent les classes des packages correspondant aux différentes parties étudiées en cours.

La classe Test.java sert à démontrer que la classe Rule fonctionne conformement à l'énoncé du TP. Elle teste également notre implémentation de l'algorithme de Backtrack sur un problème court.
La classe HealthCare.java génère un problème "médical" comme indiqué dans le sujet de TP sur la planninfication (on crée un état aléatoire, des médicaments expérimentaux, une affectation "final"). En exécutant le main de HealthCare, on génére le problème on teste les différents algorithmes de recherches de chemin vu en cours (dfs, bfs, Dijkstra, A*) dont les résultats s'affichent dans la console. Note sur Dijkstra : l'algorithme trouve bien un chemin avec les "sirops" (actions diminuant d'un seul niveau une variable) mais ne s'arrête pas lorsque que l'on utilise les actions ("médicaments") créées aléatoirement. On suppose que c'est le fait qu'un état final puisse aussi appliquer ces actions (préconditions vides) qui empêche Dijkstra de se terminer. On peut tester la différence en commentant la ligne (ne créant du coup que les sirops):

-actions.addAll(hc.makeNMedecine(20, 3, 2));

D'autres classes de tests mineurs ont été implémentées dans les packages correspondant aux parties du TP étudiées.

### Compilation et lancement

Les classes de test du projet peuvent être directement compilées et exécutées en ligne de commande grâce au compilateur javac et à java. 

Par exemple pour lancer la classe examples.Test.java il suffit de saisir :

```sh
$ cd {Dossier racine du projet}
$ javac examples/Test.java
$ java examples.Test.java
```
Bonne compilation !
