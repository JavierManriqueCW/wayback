//
//  WaybackWidgetLiveActivity.swift
//  WaybackWidget
//
//  Created by Javier Manrique Pellejero on 24/11/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import ActivityKit
import WidgetKit
import SwiftUI

struct WaybackWidgetAttributes: ActivityAttributes {
    public struct ContentState: Codable, Hashable {
        // Dynamic stateful properties about your activity go here!
        var emoji: String
    }

    // Fixed non-changing properties about your activity go here!
    var name: String
}

struct WaybackWidgetLiveActivity: Widget {
    var body: some WidgetConfiguration {
        ActivityConfiguration(for: WaybackWidgetAttributes.self) { context in
            // Lock screen/banner UI goes here
            VStack {
                Text("Hello \(context.state.emoji)")
            }
            .activityBackgroundTint(Color.cyan)
            .activitySystemActionForegroundColor(Color.black)

        } dynamicIsland: { context in
            DynamicIsland {
                // Expanded UI goes here.  Compose the expanded UI through
                // various regions, like leading/trailing/center/bottom
                DynamicIslandExpandedRegion(.leading) {
                    Text("Leading")
                }
                DynamicIslandExpandedRegion(.trailing) {
                    Text("Trailing")
                }
                DynamicIslandExpandedRegion(.bottom) {
                    Text("Bottom \(context.state.emoji)")
                    // more content
                }
            } compactLeading: {
                Text("L")
            } compactTrailing: {
                Text("T \(context.state.emoji)")
            } minimal: {
                Text(context.state.emoji)
            }
            .widgetURL(URL(string: "http://www.apple.com"))
            .keylineTint(Color.red)
        }
    }
}

extension WaybackWidgetAttributes {
    fileprivate static var preview: WaybackWidgetAttributes {
        WaybackWidgetAttributes(name: "World")
    }
}

extension WaybackWidgetAttributes.ContentState {
    fileprivate static var smiley: WaybackWidgetAttributes.ContentState {
        WaybackWidgetAttributes.ContentState(emoji: "😀")
     }
     
     fileprivate static var starEyes: WaybackWidgetAttributes.ContentState {
         WaybackWidgetAttributes.ContentState(emoji: "🤩")
     }
}

#Preview("Notification", as: .content, using: WaybackWidgetAttributes.preview) {
   WaybackWidgetLiveActivity()
} contentStates: {
    WaybackWidgetAttributes.ContentState.smiley
    WaybackWidgetAttributes.ContentState.starEyes
}
