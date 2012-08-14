/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.collections.top;

/**
 * The BufferOverflowException is used when the buffer's capacity has been
 * exceeded.
 *
 * @since Commons Collections 2.1
 * @version $Revision: 814127 $ $Date: 2009-09-12 04:45:33 -0500 (Sat, 12 Sep 2009) $
 *
 * @author Avalon
 * @author <a href="mailto:bloritsch@apache.org">Berin Loritsch</a>
 * @author <a href="mailto:jefft@apache.org">Jeff Turner</a>
 * @author Paul Jack
 * @author Stephen Colebourne
 */
public class BufferOverflowException extends RuntimeException {
    
    /** Serialization version */
    private static final long serialVersionUID = -3992254982265755876L;

    /** The root cause throwable */
    private final Throwable throwable;

    /**
     * Constructs a new <code>BufferOverflowException</code>.
     */
    public BufferOverflowException() {
        super();
        throwable = null;
    }

    /** 
     * Construct a new <code>BufferOverflowException</code>.
     * 
     * @param message  the detail message for this exception
     */
    public BufferOverflowException(String message) {
        this(message, null);
    }

    /** 
     * Construct a new <code>BufferOverflowException</code>.
     * 
     * @param message  the detail message for this exception
     * @param exception  the root cause of the exception
     */
    public BufferOverflowException(String message, Throwable exception) {
        super(message);
        throwable = exception;
    }

    /**
     * Gets the root cause of the exception.
     *
     * @return the root cause
     */
    public final Throwable getCause() {
        return throwable;
    }
    
}
