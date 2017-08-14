/*******************************************************************************
 * Copyright (c) 2012-2017 TypeFox GmbH and itemis AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 *******************************************************************************/

package org.xpect.state;

import org.xpect.text.CharSequences;

public class ManagedImpl<T> implements Managed<T> {

	private final T value;

	public ManagedImpl(T value) {
		super();
		this.value = value;
	}

	public T get() {
		return value;
	}

	public void invalidate() {
	}

	@Override
	public String toString() {
		return "Managed[" + CharSequences.toSingleLineString(value, 80) + "]";
	}

}
