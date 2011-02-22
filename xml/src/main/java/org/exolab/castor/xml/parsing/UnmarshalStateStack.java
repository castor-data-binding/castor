/*
 * Copyright 2005 Philipp Erlacher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exolab.castor.xml.parsing;

import java.util.Stack;

import org.exolab.castor.xml.UnmarshalState;

/**
 * This class helps to access a stack in that {@link UnmarshalState}s are stored.
 * 
 * @author <a href="mailto:philipp DOT erlacher AT gmail DOT com">Philipp
 *         Erlacher</a>
 * 
 */
public class UnmarshalStateStack {

	/**
	 * The stack to store {@link UnmarshalState}s.
	 */
	private Stack<UnmarshalState> _unmarshalStates = new Stack<UnmarshalState>();

	/**
	 * This index denotes the position of the parent state.
	 */
	private Integer parentStateIndex = null;

	/**
	 * Peeks the stack for the top {@link UnmarshalState}, without removing 
	 * it.
	 * 
	 * @return Top {@link UnmarshalState}, without removing it.
	 */
	public UnmarshalState getLastState() {
		return _unmarshalStates.peek();
	}

	/**
	 * Pops the top {@link UnmarshalState} off the stack.
	 * 
	 * @return Top {@link UnmarshalState} instance, removing it from the stack as well.
	 */
	public UnmarshalState removeLastState() {
		return _unmarshalStates.pop();
	}

	/**
	 * Checks if the stack is empty.
	 * 
	 * @return True if there is no element on the stack.
	 */
	public boolean isEmpty() {
		return _unmarshalStates.empty();
	}

	/**
	 * Pushes a {@link UnmarshalState} instance onto the stack-
	 * 
	 * @param state The {@link UnmarshalState} instance to be pushed onto the stack.
	 */
	public void pushState(UnmarshalState state) {
		_unmarshalStates.push(state);
	}

	/**
	 * Checks if there is a parent state on the stack.
	 * 
	 * @return True of there's a parent state.
	 */
	public boolean hasAnotherParentState() {
		if (parentStateIndex == null) {
			parentStateIndex = _unmarshalStates.size() - 2;
		}
		return parentStateIndex >= 0;
	}

	/**
	 * Removes a parent state from the stack.
	 * 
	 * @return UnmarshalState that is a parent state
	 */
	public UnmarshalState removeParentState() {
		Integer tmpParentIndex = parentStateIndex;
		parentStateIndex--;
		return _unmarshalStates.elementAt(tmpParentIndex);
	}
	
	public void resetParentState() {
	    this.parentStateIndex = null;
	}

}
