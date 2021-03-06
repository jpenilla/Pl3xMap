import { Pin } from "./util/Pin.js";
import { Fieldset } from "./util/Fieldset.js";
import { P } from './Pl3xMap.js';

class Sidebar {
    constructor(pinned) {
        this.sidebar = P.createElement("div", "sidebar", this);
        document.getElementById("map").appendChild(this.sidebar);

        if (pinned != "hide") {
            this.pin = new Pin(pinned == "pinned");
            this.sidebar.appendChild(this.pin.element);
            this.show(this.pin.pinned);
        }

        this.worldList = new Fieldset("worlds", "Worlds");
        this.sidebar.appendChild(this.worldList.element);

        this.playerList = new Fieldset("players", "Players");
        this.sidebar.appendChild(this.playerList.element);

        this.sidebar.onmouseleave = () => {
            if (!this.pin.pinned) {
                this.show(false);
            }
        };
        this.sidebar.onmouseenter = () => {
            if (!this.pin.pinned) {
                this.show(true);
            }
        };
    }
    show(show) {
        this.sidebar.className = show ? "show" : "";
    }
}

export { Sidebar };
