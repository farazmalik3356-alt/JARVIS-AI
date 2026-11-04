package com.jarvis.ai.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast

class JarvisAccessibilityService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        Toast.makeText(this, "Jarvis ab aapka phone control kar sakti hai, jaan!", Toast.LENGTH_LONG).show()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Yahan AI ke commands receive honge (baad mein)
    }

    override fun onInterrupt() {}

    // Yeh function AI ke kehne par kisi bhi text (button) par tap karega
    fun tapOnText(text: String): Boolean {
        val root = rootInActiveWindow ?: return false
        val nodes = root.findAccessibilityNodeInfosByText(text)
        if (nodes.isNotEmpty()) {
            nodes[0].performAction(AccessibilityNodeInfo.ACTION_CLICK)
            return true
        }
        return false
    }

    // Yeh function kisi bhi app ko kholne ke liye (AI command ke zariye)
    fun openApp(packageName: String) {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}
