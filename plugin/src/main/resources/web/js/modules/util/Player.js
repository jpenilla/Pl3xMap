import { P } from '../Pl3xMap.js';

class Player {
    constructor(player) {
        this.name = player.name;
        this.uuid = player.uuid;
        this.world = player.world;
        this.x = 0;
        this.z = 0;
        this.marker = L.marker(P.toLatLng(player.x, player.z), {
            icon: L.icon({
                iconUrl: 'images/icon/player.png',
                iconSize: [17, 16],
                iconAnchor: [8, 9],
                tooltipAnchor: [0, 0]
            }),
            rotationAngle: (180 + player.yaw)
        });
        if (P.worldList.curWorld.player_tracker.nameplates.enabled) {
            const tooltip = L.tooltip({
                permanent: true,
                direction: "right",
                offset: [10, 0],
                pane: "nameplate"
            });
            let headImg = "";
            if (P.worldList.curWorld.player_tracker.nameplates.show_heads) {
                headImg = `<img src='${P.getHeadUrl(player)}' />`;
            }
            tooltip.setContent(`${headImg}<span>${player.name}</span>`);
            this.marker.bindTooltip(tooltip);
        }
    }
    update(player) {
        this.x = player.x;
        this.z = player.z;
        this.world = player.world;
        const link = document.getElementById(player.uuid);
        const img = link.getElementsByTagName("img")[0];
        const span = link.getElementsByTagName("span")[0];
        if (P.worldList.curWorld.name == player.world) {
            this.marker.addTo(P.layerControl.playersLayer);
            const latlng = P.toLatLng(player.x, player.z);
            if (!this.marker.getLatLng().equals(latlng)) {
                this.marker.setLatLng(latlng);
            }
            const angle = 180 + player.yaw;
            if (this.marker.options.rotationAngle != angle) {
                this.marker.setRotationAngle(angle);
            }
            img.classList.remove("other-world");
            span.classList.remove("other-world");
        } else {
            this.marker.remove();
            img.classList.add("other-world");
            span.classList.add("other-world");
        }
    }
}

export { Player };
