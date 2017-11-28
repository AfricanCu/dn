package com.wk.util;

import org.jdeferred.AlwaysCallback;
import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.jdeferred.ProgressCallback;

public interface PromiseI<D, F, P> extends DoneCallback<D>, FailCallback<F>,
		ProgressCallback<P>, AlwaysCallback<D, P> {
}