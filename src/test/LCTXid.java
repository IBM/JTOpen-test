// //////////////////////////////////////////////////////////////////////
//------------------- Maintenance-Change Activity ------------------
//
//Flag Reason Rel Lvl Date PGMR Comments
//---- -------- ------- -------- ------- ---------------------------
//                          11/01/05 pauldev create
////////////////////////////////////////////////////////////////////////
package test;

import java.util.*;

/*
 * Implements needed Xid for loosely coupled transaction support. To get a
 * loosely coupled transaction branch, the 16 byte gtrids must be the same
 * between two XIDs, but only the first 16 bytes of the 48 byte bquals are
 * equal.
 */
public class LCTXid implements  // @A1C
        javax.transaction.xa.Xid // @A1A
{
    protected final static int FMTID = 1; //42;

    // Default to the NULL XID.
    protected int formatId = -1;

    protected byte gtrid[] = new byte[16]; 
    protected byte bqual[] = new byte[48]; 

    protected long myGtrid;

    protected long myBqual;


    // These guys are used as a mechanism to generate new and unusued
    // transaction IDs.
    protected static Object lockGen = new Object();

    protected static long gtridGen = 1;

    /* clone this object and then change a few of the upper bqual bytes to make unique.
       The 16 byte gtrids must be the same
       between two XIDs, but only the first 16 bytes of the 48 byte bquals are equal. */
    public LCTXid getMatchingLooselyCoupledXid() {
        
        LCTXid newXid = new LCTXid();
        newXid.setLooselyCoupledParts(this);
        return newXid;
    }
    
    /** The format ID used by all TestXid objects */
    public final static int theFormatId() {
        return FMTID;
    }

    private void setByteValues() {
        //get eight bytes of long type myGtrid. other 8 bytes are 0
        gtrid[0] = (byte) (0xFF & (myBqual >> 56));
        gtrid[1] = (byte) (0xFF & (myBqual >> 48));
        gtrid[2] = (byte) (0xFF & (myBqual >> 40));
        gtrid[3] = (byte) (0xFF & (myBqual >> 32));
        gtrid[4] = (byte) (0xFF & (myBqual >> 24));
        gtrid[5] = (byte) (0xFF & (myBqual >> 16));
        gtrid[6] = (byte) (0xFF & (myBqual >> 8));
        gtrid[7] = (byte) (0xFF & (myBqual >> 0));

        //get eight bytes of long type myBqual timestamp
     
        bqual[0] = (byte) (0xFF & (myBqual >> 56));
        bqual[1] = (byte) (0xFF & (myBqual >> 48));
        bqual[2] = (byte) (0xFF & (myBqual >> 40));
        bqual[3] = (byte) (0xFF & (myBqual >> 32));
        bqual[4] = (byte) (0xFF & (myBqual >> 24));
        bqual[5] = (byte) (0xFF & (myBqual >> 16));
        bqual[6] = (byte) (0xFF & (myBqual >> 8));
        bqual[7] = (byte) (0xFF & (myBqual >> 0));
        //duplicate 8 byte timestamp for all 48 bytes
        for (int x = 8; x < 48; x++) {
            bqual[x] = bqual[x % 8];
        }
    }

    private void setLooselyCoupledParts(LCTXid xid) {
        
        System.arraycopy(xid.getGlobalTransactionId(), 0, this.gtrid, 0, 16); //all 16 bytes same
        System.arraycopy(xid.getBranchQualifier(), 0, this.bqual, 0, 16); //only first 16 bytes same
        //remaining 17-48 bytes of bqual are already set with new timestamp
    }

    /**
     * Generate a new/unused transaction identifier
     */
    public LCTXid(int f) {
        synchronized (lockGen) {
            myGtrid = gtridGen;
            // myBqual = bqualGen;
            ++gtridGen;
            // ++bqualGen;
            Date time = new Date();
            myBqual = time.getTime();
            // Just a safeguard under protection of the lock so there is no
            // possible way to get two transactions with the same millisecond
            // branch qualifier.
            try {
                Thread.sleep(50);//for some java reason, sleep(1) will sometimes get dup timestamps
            } catch (InterruptedException e) {}
        }
        if (f != -1) {
            formatId = f;
        } else {
            formatId = theFormatId();
        }
        setByteValues();
    }

    public LCTXid() {
        this(theFormatId());
    }

    public void setFormatId(int f) {
        formatId = f;
    }

    public int getFormatId() {
        return formatId;
    }

    public byte[] getGlobalTransactionId() {
        return gtrid;
    }

    public byte[] getBranchQualifier() {
        return bqual;
    }


}
