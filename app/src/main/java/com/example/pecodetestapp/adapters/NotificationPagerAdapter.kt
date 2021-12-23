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
import androidx.core.app.TaskStackBuilder
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.pecodetestapp.MainActivity
import com.example.pecodetestapp.R


class NotificationPagerAdapter(private val context: Context, private val fragments: List<Fragment>):
    RecyclerView.Adapter<NotificationPagerAdapter.PagerViewHolder>() {

    class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notificationButton: Button = itemView.findViewById(R.id.notificationButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        return PagerViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_notification, parent, false))
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.notificationButton.setOnClickListener {
            createNotificationChannel()
            showNotification(position)
            Log.e("TAG", "Shown") }
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    private fun showNotification(position: Int) {
        val channelId = "all_notifications"
        val resultIntent = Intent(context, MainActivity::class.java)
        resultIntent.putExtra("notification_id", position)

        val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(MainActivity::class.java)
        stackBuilder.addNextIntent(resultIntent)

        val pendingIntent: PendingIntent? =
            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_baseline_add_24)
            .setContentTitle("You create a notification")
            .setContentText(String.format("Notification %d", position + 1))
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