/**
 * 
 */
package pl.polsl.parser;

import java.util.List;

import pl.polsl.architecture.WMachine;

/**
 * @author Dawid Poloczek
 * @version 1.0
 */
public class SetOfSignals implements ITact {

	private List<Integer> activeSignals;
	
	protected void setSignalsList(List<Integer> signalsList){
		this.activeSignals = signalsList;
	}
	
	public List<Integer> getSignalsList(){
		return this.activeSignals;
	}
	
	@Override
	public void run(WMachine machine) {
	}
}
