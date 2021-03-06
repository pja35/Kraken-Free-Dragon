package model;

import java.util.LinkedList;
import java.util.List;

import view.GraphicExpressionFactory;

/**
 * Cette classe est la principale classe à utiliser pour quicqonque
 * souhaite se servir de Kraken-Free-Dragon.
 * Elle dipose d'outils pour maintenir une expression sous forme d'arbre
 * qui peut subir des manipulations sous forme de règles.
 * @author Pacôme
 *
 */
public class KrakenTree {
	private Expression root;
	
	/**
	 * Ce constructeur initialise la librairie en utilisant la factory utilisateur.
	 * Cette factory permet de faire le lein entre le support physique et l'interface
	 * graphique développée indépendament.
	 * @param factory : la factory utilisateur
	 * @see view.GraphicExpressionFactory
	 */
	public KrakenTree(GraphicExpressionFactory factory) {
		Configuration.init(factory);
		
	}
	
	/**
	 * Affecte la racine à l'expression voulue.
	 * @param root : la nouvelle racine
	 */
	public void setRoot(Expression root) {
		this.root = root;
		root.setFather(null);
	}
	
	/**
	 * Retourne la racine. Peut être null
	 * @return la racine, si elle existe
	 */
	public Expression getRoot() {
		return root;
	}
	
	/**
	 * Applique la règle rule à l'expression en argument.
	 * La fonction Rule.applic() retournant un objet différent
	 * de l'expression initiale, il est important de différencier les cas
	 * où l'expression sur laquelle on opère est la racine, ou non.
	 * Dans le cas où l'expression est la racine, il suffit de remplacer
	 * la racine par le résultat de la règle.
	 * Dans le cas contraire, il faut retrouver le père de l'expression,
	 * puis remplacer le fils adécquat par le résultat de la règle.
	 * @param expression : l'expression sur laquelle appliquer la règle
	 * @param rule : la règle à appliquer
	 * @see model.Rule
	 */
	public void applicRule(Expression expression, Rule rule) {
		if(expression == root) {
			setRoot(rule.applic(root));
		}
		else {
			Expression father = expression.getFather();
			if( father instanceof BinaryExpression ) {
				BinaryExpression b_father = (BinaryExpression) father;
				if( b_father.firstExpression() == expression )
					b_father.setFirstExpression(rule.applic(expression));
				else
					b_father.setSecondExpression(rule.applic(expression));
			}
			else {
				UnaryExpression u_father = (UnaryExpression) father;
				u_father.setSubExpression(rule.applic(expression));
			}
		}
	}
	
	/**
	 * Fonction statique permettant de récupérer la racine d'une root_path.
	 * La racine est simplement le dernier élément de la liste.
	 * @param root_path : le root path
	 * @return la racine
	 */
	public static Expression getRootFromRootPath(List<Expression> root_path) {
		if( root_path.isEmpty() ) throw new IllegalArgumentException();
		return root_path.get( root_path.size() - 1 );
	}
	
	/**
	 * Prend les deux root_path en argument et renvoit la partie commune des deux.
	 * Permet de retrouver la racine la plus basse de deux éléments dans l'arbre à
	 * partir de leur path_root.
	 * @param path_list_1 : le premier chemin
	 * @param path_list_2 : le second chemin
	 * @return le chemin en commun
	 */
	public static List<Expression> deduceRootPath(List<Expression> path_list_1, List<Expression> path_list_2) {
		int k = 0;
		for(; k < path_list_1.size() && k < path_list_2.size(); k++)
			if( path_list_1.get(k) != path_list_2.get(k) )
				return path_list_1.subList(0, k);
		
		return path_list_1.subList(0, k);
	}
	
	/**
	 * Renvoit la plus basse racine commune entre n noeuds de l'arbre.
	 * Cette fonction procède d'abord à retrouver les chemins depuis la racine
	 * vers tout les noeuds dans la liste.
	 * Puis, elle trouve depuis la racine, le chemin le plus long commun à tout les noeuds.
	 * Enfin, elle récupère le dernier élément de cette liste et le renvoit.
	 * @param expr : une liste de noeuds
	 * @throws IllegalArgumentException si la liste est vide
	 * @return le noeud le plus bas père de tout les noeuds de la liste
	 */
	public static Expression deduceRoot(List<Expression> expr) {
		if( expr.isEmpty() ) throw new IllegalArgumentException();
		if( expr.size() == 1 ) return expr.get(0); 
		
		List<Expression> path_list = expr.get(0).generatePathList();
		
		for(int k = 1; k < expr.size(); k++) {
			path_list = deduceRootPath(path_list, expr.get(k).generatePathList());
		}
		
		return getRootFromRootPath(path_list);
	}
	
	/**
	 * Cette fonction renvoit toutes les combinaisons possibles de règles appliquable
	 * sur le noeud expression avec le type d'input input_type.
	 * Cette fonction est statique car elle n'a besoin que de la configuration
	 * des règles, qui est maintenue dans la référence statique Configuraion.rules.
	 * @param input_type : le type d'input
	 * @param expr : le noeud cible
	 * @return une paire contenant le noeud cible, et la liste des règles applicables
	 */
	public static Pair<Expression, List<Rule>> processInput(String input_type, Expression expr) {
		List<Rule> raw_rule_list = Configuration.rules.getRuleList(input_type);
		List<Rule> rules = new LinkedList<Rule>();
		for(Rule rule : raw_rule_list)
			if( rule.canApplic(expr) ) rules.add(rule);
		
		return new Pair<Expression, List<Rule>>(expr, rules);
	}
	
	/**
	 * Cette fonction procède à la même chose que la précédente, à l'exception près
	 * qu'elle déduit le noeud cible à partir d'une liste de noeuds en extrayant
	 * la racine la plus basse commune à tout les noeuds.
	 * @param input_type : le type d'input
	 * @param exprs : les noeuds cibles
	 * @return une paire contenant le noeud cible, et la liste des règles applicables
	 * @see deduceRoot
	 * @see processInput
	 */
	public static Pair<Expression, List<Rule>> processInput(String input_type, List<Expression> exprs) {
		Expression root = deduceRoot(exprs);
		List<Rule> raw_rule_list = Configuration.rules.getRuleList(input_type);
		List<Rule> rules = new LinkedList<Rule>();
		for(Rule rule : raw_rule_list)
			if( rule.canApplic(root) ) rules.add(rule);
		
		return new Pair<Expression, List<Rule>>(root, rules);
	}
}
