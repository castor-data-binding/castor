/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id $
 */
package org.exolab.castor.persist.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.ref.ReferenceQueue;

/**
 * A Timer generates ticks periodically. To listen to ticks, a listener adds 
 * itself using the {@link addTimerListener} method. 
 * <p>
 * This timer implementation links to listeners by {java.lang.ref.WeakReference} 
 * so that the timer will not prohibit the listeners from being garbage collected.
 * <p>
 * @author <a href="yip@intalio.com">Thomas Yip</a>
 */
public final class Timer extends Thread {

    // --- instance stuffs ---
    private ReferenceQueue refQ = new ReferenceQueue();

    private ArrayList listeners = new ArrayList();

    private long period;

    private boolean stopped;

    public Timer( int period ) {
        this.period = period;
        setDaemon( true );
        start();
    }

    public void run() {
        long lasttime = System.currentTimeMillis();
        while ( !stopped ) {
            long wantedTime = lasttime + period;
            long sleepMore = wantedTime - System.currentTimeMillis();
            if ( sleepMore > 0 ) {
                try {
                    sleep( sleepMore );
                } catch ( InterruptedException e ) {
                    if ( stopped ) break;
                }
            }
            lasttime = wantedTime;
            Iterator itor = nullItor;
            synchronized( listeners ) {
                cleanup();
                if ( listeners.size() > 0 ) {
                    itor = ((ArrayList)listeners.clone()).iterator();
                }
            }
            while( itor.hasNext() ) {
                WeakReference wr = (WeakReference) itor.next();
                TimeListener tl = (TimeListener) wr.get();
                if ( tl != null ) {
                    tl.tick();
                }
            }
        }
    }

    public void stopTimer() {
        stopped = true;
    }

    public void addTimeListener( TimeListener listener ) {
        synchronized( listeners ) {
            listeners.add( new WeakReference( listener, refQ ) );
        }
    }

    public void removeTimeListener( TimeListener listener ) {
        synchronized( listeners ) {
            Iterator itor = listeners.iterator();
            while ( itor.hasNext() ) {
                WeakReference wr = (WeakReference)itor.next();
                TimeListener tl = (TimeListener) wr.get();
                itor.remove();
            }
        }
    }

    /**
     * Should be synchronized with listeners before calling this method
     */
    private void cleanup() {
        synchronized( listeners ) {
            Reference wr;
            while ( (wr = refQ.poll()) != null ) {
                listeners.remove( wr );
            }
        }
    }

    // --- static stuffs ---
    public static interface TimeListener {
        public void tick();
    }

    private static Iterator nullItor = new Iterator() {
        public boolean hasNext() {
            return false;
        }
        public Object next() throws NoSuchElementException {
            throw new NoSuchElementException();
        }
        public void remove() throws IllegalStateException {
            throw new IllegalStateException();
        }
    };

    private static class Test implements TimeListener {
        public static void main( String args[] ) throws Exception {
            Timer t = new Timer( 1000 );
            t.addTimeListener( new Test() );
            Thread.currentThread().sleep( 10 * 1000 );
        }
        public void tick() {
            System.out.println( System.currentTimeMillis() );
        }
    }
}
