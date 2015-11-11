package model;

public class Rule {

	Expression input_model;
	Expression result_model;
	
	public Rule(Expression input_model, Expression result_model) {
		this.input_model = input_model;
		this.result_model = result_model;
	}
	
	public boolean canApplic(Expression input) {
		if( ! input.doesMatchModel(input_model) ) return false;
		
		Memory memory = new Memory();
		
		if( ! memory.init(input_model, input) ) return false;
		
		return true;
	}
	
	public Expression applic(Expression input) throws IllegalArgumentException
	{
		if( ! input.doesMatchModel(input_model) ) throw new IllegalArgumentException();
		
		Memory memory = new Memory();
		
		if( ! memory.init(input_model, input) ) throw new IllegalArgumentException();
		
		return memory.applic(result_model);
	}
	
	@Override
	public String toString() {
		return input_model.expressionToString() + " ==> " + result_model.expressionToString();
	}
	
	public Expression getInputModel() {
		return input_model;
	}
	
	public Expression getResultModel() {
		return result_model;
	}
	
	public Object generateExpression() {
		return Configuration.graphic.generateRuleExpression(this);
	}

}
