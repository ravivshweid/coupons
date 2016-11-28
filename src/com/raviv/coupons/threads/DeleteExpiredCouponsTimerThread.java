package com.raviv.coupons.threads;

import java.sql.Timestamp;

import com.raviv.coupons.utils.PrintUtils;

/**
 * 
 * Delete expired coupons thread
 * 
 * @author raviv
 *
 */
public class DeleteExpiredCouponsTimerThread extends Thread 
{
	
	@Override
	public  void run()
	{	
		DeleteExpiredCouponsThread deleteExpiredCouponsThread = new DeleteExpiredCouponsThread();
		Timestamp timestamp;
		
		// =========================================================
		// Infinite loop 
		// =========================================================
		while ( true )
		{
			timestamp = new Timestamp(System.currentTimeMillis());
			PrintUtils.printHeader("DeleteExpiredCouponsTimerThread started " + timestamp );
			// =========================================================
			// delete expired coupons 
			// =========================================================
			deleteExpiredCouponsThread.start();

			// =========================================================
			// Pause for 24 hours
			// =========================================================
			PrintUtils.printHeader("DeleteExpiredCouponsTimerThread go to sleep 24 hours");
            try 
            {
            	long ms = 3600 * 24 ;
				Thread.sleep(ms);
			} 
            catch (InterruptedException e) 
            {
				e.printStackTrace();
			}						
		}// while end
				
	}

}