package com.example.pecodetestapp.adapters

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pecodetestapp.MainActivity
import com.example.pecodetestapp.R

class NotificationPagerAdapter(private val context: Context, private val fragments: List<Fragment>):
    RecyclerView.Adapter<NotificationPagerAdapter.PagerViewHolder>() {

    class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notificationButton  = itemView.findViewById<Button>(R.id.notificationButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        return PagerViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_notification, parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.notificationButton.setOnClickListener {
            createNotificationChannel()
            showContinueRegistrationNotification(position)
            Log.e("TAG", "Shown") }
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun showContinueRegistrationNotification(position: Int) {
        val channelId = "all_notifications" // Use same Channel ID
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this.context, 0, intent, 0);
        val builder = NotificationCompat.Builder(context, channelId) // Create notification with channel Id
            .setSmallIcon(R.drawable.ic_baseline_add_24)
            .setContentTitle("My notification")
            .setContentText("Notification $position")
            .setPriority(NotificationCompat.PRIORITY_MAX)
        builder.setContentIntent(pendingIntent).setAutoCancel(true)
        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        with(mNotificationManager) {
            notify(position, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "all_notifications"
            val mChannel = NotificationChannel(
                channelId,
                "General Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            mChannel.description = "This is default channel used for all other notifications"

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

}