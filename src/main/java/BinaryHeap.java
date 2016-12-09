///////////////////////////////////////////////////////////////////////////////////////////////////
//
// BinaryHeap.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////////////////////////
public class BinaryHeap
{
	private Comparable[] m_heap = null;
	private int m_size = 0;

	private static final int DEFAULT_CAPACITY = 16;

	///////////////////////////////////////////////////////////////////////////////////////////////
	public BinaryHeap( int cap )
	{
		m_size = 0;
		m_heap = new Comparable[cap];
	}

	public BinaryHeap()
	{
		this( DEFAULT_CAPACITY );
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	public boolean isEmpty()
	{
		return m_size == 0;
	}
	
	public int size()
	{
		return m_size;
	}

	public void clear()
	{
		m_size = 0;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void push( Comparable x )
	{
		// Add an item (sorted) to the heap.
		if( m_size == m_heap.length )
		{
			inflate();
		}

		m_heap[m_size] = x;
		++m_size;
		percolateDown( m_size - 1 );
	}

	public Comparable peek()
	{
		// Returns the min item.
		return m_heap[0];
	}

	public Comparable pop()
	{
		// Pull the min item out of the heat and re-sort.
		Comparable item = m_heap[0];

		--m_size;
		m_heap[0] = m_heap[m_size];
		percolateUp( 0 );
		
		return item;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public Comparable repush( int idx )
	{
		// Remove and re-insert the item at the passed index.  Use when sortable criteria changes.
		Comparable item = m_heap[idx];
		
		// Pull entries up from the root to fill the now empty node.
		for( ; idx > 0; idx = (idx-1) / 2 )
		{
			m_heap[idx] = m_heap[(idx-1) / 2];
		}
		
		// Insert the pushed item at the base and percolate up.
		m_heap[0] = item;
		percolateUp( 0 );
		
		return item;
	}
	
	public Comparable remove( int idx )
	{
		// Remove an item from the middle of the heap.
		Comparable item = m_heap[idx];
		
		// Insert the last item into the newly open space and re-push it.
		--m_size;
		m_heap[idx] = m_heap[m_size];
		repush( idx );
		
		return item;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public int find( Comparable c )
	{
		// Return the index of the passed entry.
		for( int i = 0; i < m_size; ++i )
		{
			if( c == m_heap[i] )
			{
				return i;
			}
		}
		
		return -1;
	}
	
	public void repush( Comparable c )
	{
		// Repush an entry rather than an index.
		int idx = find( c );
		if( idx >= 0 )
		{
			repush( idx );
		}
	}
	
	public void remove( Comparable c )
	{
		// Remove an entry rather than an index.
		int idx = find( c );
		if( idx >= 0 )
		{
			remove( idx );
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	private void inflate()
	{
		// Double the size of the heap.
		Comparable[] old = m_heap;
		m_heap = new Comparable[old.length * 2];
		
		for( int i = 0; i < m_size; ++i )
		{
			m_heap[i] = old[i];
		}
	}
	
	private int percolateUp( int idx )
	{
		Comparable x = m_heap[idx];
		
		// Percolate up (from root to leaves) from the passed index.
		while( (idx * 2) + 1 < m_size )
		{
			int child = (idx * 2) + 1;
			if( child < m_size - 1 && m_heap[child+1].compareTo( m_heap[child] ) < 0 )
			{
				++child;
			}
			
			if( x.compareTo( m_heap[child] ) < 0 )
			{
				// All done.
				break;
			}
			
			m_heap[idx] = m_heap[child];
			idx = child;
		}
		
		m_heap[idx] = x;
		return idx;
	}
	
	private int percolateDown( int idx )
	{
		// Percolate down (from leaves to root) from the passed index.
		Comparable x = m_heap[idx];
		for( ; idx > 0 && x.compareTo( m_heap[(idx-1) / 2] ) < 0; idx = (idx-1) / 2 )
		{
			m_heap[idx] = m_heap[(idx-1) / 2];
		}
		
		m_heap[idx] = x;
		return idx;
	}
}
