/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.architecture;

/**
 * Interface of data target. If component of W Machine should be able
 * to get value from a data source when accurate signal is activated then
 * it must implement this interface.
 * @author Tomasz Rzepka
 * @version 1.0
 */
public interface DataTarget extends WMachineComponent {
	/**
	 * Function to set value for this W Machine component.
	 * @param value a value to be set.
	 * @throws Exception when error occurs
	 */
    public void setValue(Integer value) throws Exception;
}
