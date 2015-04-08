package pl.polsl.architecture;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import pl.polsl.architecture.components.DataSource;
import pl.polsl.architecture.components.DataTarget;
import pl.polsl.architecture.components.finalized.ArithmeticLogicUnit;
import pl.polsl.architecture.components.finalized.Buffer;
import pl.polsl.architecture.components.finalized.Bus;
import pl.polsl.architecture.components.finalized.Memory;
import pl.polsl.architecture.components.finalized.Register;
import pl.polsl.architecture.signals.ScriptSignal;
import pl.polsl.architecture.signals.Signal;
import pl.polsl.servlet.ArchitectureInfo.AvailableRegisters;
import pl.polsl.servlet.ArchitectureInfo.AvailableSignals;
import pl.polsl.utils.Primitive;

public class WMachineBuilder {

	private WMachine machine;
	private ScriptEngine engine;
	private Primitive<Integer> addressBitCount;
	private Primitive<Integer> dataBitCount;
	
	public WMachineBuilder() {
		machine = null;
		engine = new ScriptEngineManager().getEngineByName("nashorn");
	}
	
	public void begin(Integer addressBitCount, Integer dataBitCount) {
		this.addressBitCount = new Primitive<>();
		this.addressBitCount.setValue(addressBitCount);
		this.dataBitCount = new Primitive<>();
		this.dataBitCount.setValue(dataBitCount);
		machine = new WMachine(this.addressBitCount, this.dataBitCount, engine);
	}
	
	public Register addRegister(String registerName, Integer bitCount) {
		try {
			Integer registerId = AvailableRegisters.valueOf(registerName).ID;
			Register register = new Register(getBitCountPrimitive(bitCount));
			machine.addRegister(registerId, register);
			return register;
		}
		catch(IllegalArgumentException ex) {
			return null;
		}
	}
	
	public Signal addSignal(String signalName, DataSource source, DataTarget target) {
		try {
			Integer signalId = AvailableSignals.valueOf(signalName).ID;
			Signal signal = new Signal(source, target);
			machine.addSignal(signalId, signal);
			return signal;
		}
		catch(IllegalArgumentException ex) {
			return null;
		}
	}
	
	public Signal addScriptSignal(String signalName, DataSource source, DataTarget target, String function) {
		try {
			Integer signalId = AvailableSignals.valueOf(signalName).ID;
			Signal signal = new ScriptSignal(source, target, function, engine);
			machine.addSignal(signalId, signal);
			return signal;
		}
		catch(IllegalArgumentException ex) {
			return null;
		}
	}
	
	public Memory addMemory(Register addressRegister) {
		Memory memory = new Memory(addressRegister, addressBitCount, dataBitCount);
		addressBitCount.addChangeListener(memory);
		machine.addComponent(memory);
		return memory;
	}
	
	public ArithmeticLogicUnit addArithmeticLogicUnit(Buffer aluInBuffer, Buffer aluOutBuffer) {
        ArithmeticLogicUnit alu = new ArithmeticLogicUnit(aluInBuffer, aluOutBuffer);
		machine.addComponent(alu);
		return alu;
	}
	
	public Bus addBus(Integer bitCount) {
		Bus bus = new Bus(getBitCountPrimitive(bitCount));
		machine.addComponent(bus);
		return bus;
	}
	
	public Buffer addBuffer(Integer bitCount) {
		Buffer buffer = new Buffer(getBitCountPrimitive(bitCount));
		machine.addComponent(buffer);
		return buffer;
	}
	
	public WMachine end() {
		WMachine wmachine = machine;
		machine = null;
		engine = null;
				
        wmachine.updateScriptContext();
		return wmachine;
	}
	
	private Primitive<Integer> getBitCountPrimitive(Integer bitCount) {
		if(bitCount == addressBitCount.getValue())
			return addressBitCount;
		else return dataBitCount;
	}
}
