package util;

import java.util.concurrent.atomic.AtomicInteger;

public class Identifier {

    /** Lazily initialized hash code */
    private volatile int hashCode;

    /** Sequence number for the identifier */
    private final long sequenceNumber;

    /** Atomic integer that counts the number of identifiers (thread safe) */
    private static final AtomicInteger count = new AtomicInteger(0);

    /**
     * Create an identifier with a sequence number.
     *
     * @param sequenceNumber The sequence number for this identifier.
     */
    private Identifier(final long sequenceNumber) { this.sequenceNumber = sequenceNumber; }

    /**
     * Get the long representation of this identifier
     *
     * @return Long representation
     */
    public long asLong() {
        return sequenceNumber;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        // For implementation details see, Josh Bloch's Effective Java, 2nd edition, page 47ff.
        if (hashCode == 0) {
            hashCode = 31 * 17 + (int) (sequenceNumber ^ (sequenceNumber >>> 32));
        }
        return hashCode;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        return (this == obj) || (obj != null && getClass() == obj.getClass()
                && sequenceNumber == ((Identifier) obj).sequenceNumber);
    }

    @Override
    public String toString() {
        return "Identifier: " + sequenceNumber;
    }

    /**
     * Generates a new, unique identifier.
     *
     * @return A new, unique identifier.
     */
    public static Identifier generateIdentifier() {
        return new Identifier(count.getAndIncrement());
    }
}
