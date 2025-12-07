package com.baritone.remastered.pathfinding;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class AStarPathfinder {
    private static final int MAX_ITERATIONS = 10000;
    private static final int MAX_PATH_LENGTH = 500;
    
    public static List<BlockPos> findPath(World world, BlockPos start, BlockPos goal) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(n -> n.fScore));
        Map<BlockPos, Node> allNodes = new HashMap<>();
        Set<BlockPos> closedSet = new HashSet<>();
        
        Node startNode = new Node(start, null, 0, heuristic(start, goal));
        openSet.add(startNode);
        allNodes.put(start, startNode);
        
        int iterations = 0;
        
        while (!openSet.isEmpty() && iterations < MAX_ITERATIONS) {
            iterations++;
            Node current = openSet.poll();
            
            if (current.pos.equals(goal)) {
                return reconstructPath(current);
            }
            
            closedSet.add(current.pos);
            
            for (BlockPos neighbor : getNeighbors(world, current.pos)) {
                if (closedSet.contains(neighbor)) continue;
                
                double tentativeGScore = current.gScore + cost(current.pos, neighbor);
                
                Node neighborNode = allNodes.get(neighbor);
                if (neighborNode == null) {
                    neighborNode = new Node(neighbor, current, tentativeGScore, tentativeGScore + heuristic(neighbor, goal));
                    allNodes.put(neighbor, neighborNode);
                    openSet.add(neighborNode);
                } else if (tentativeGScore < neighborNode.gScore) {
                    openSet.remove(neighborNode);
                    neighborNode.parent = current;
                    neighborNode.gScore = tentativeGScore;
                    neighborNode.fScore = tentativeGScore + heuristic(neighbor, goal);
                    openSet.add(neighborNode);
                }
            }
        }
        
        return null; // No path found
    }
    
    private static List<BlockPos> getNeighbors(World world, BlockPos pos) {
        List<BlockPos> neighbors = new ArrayList<>();
        
        // Horizontal movement
        BlockPos[] horizontalOffsets = {
            pos.north(), pos.south(), pos.east(), pos.west(),
            pos.north().east(), pos.north().west(), pos.south().east(), pos.south().west()
        };
        
        for (BlockPos offset : horizontalOffsets) {
            if (isWalkable(world, offset)) {
                neighbors.add(offset);
            } else if (isWalkable(world, offset.up())) {
                // Can step up one block
                neighbors.add(offset.up());
            }
        }
        
        // Falling down
        if (isWalkable(world, pos.down())) {
            neighbors.add(pos.down());
        }
        
        // Jump up
        if (isWalkable(world, pos.up(2))) {
            neighbors.add(pos.up(2));
        }
        
        return neighbors;
    }
    
    private static boolean isWalkable(World world, BlockPos pos) {
        // Check if feet and head positions are passable
        BlockState feetBlock = world.getBlockState(pos);
        BlockState headBlock = world.getBlockState(pos.up());
        BlockState groundBlock = world.getBlockState(pos.down());
        
        // Can't walk through solid blocks
        if (!feetBlock.isAir() && !feetBlock.isReplaceable()) return false;
        if (!headBlock.isAir() && !headBlock.isReplaceable()) return false;
        
        // Need solid ground beneath (or liquid for swimming)
        if (groundBlock.isAir()) return false;
        
        // Avoid lava
        if (feetBlock.getFluidState().isStill() && feetBlock.toString().contains("lava")) return false;
        if (groundBlock.toString().contains("lava")) return false;
        
        return true;
    }
    
    private static double heuristic(BlockPos a, BlockPos b) {
        // Euclidean distance
        int dx = a.getX() - b.getX();
        int dy = a.getY() - b.getY();
        int dz = a.getZ() - b.getZ();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    
    private static double cost(BlockPos a, BlockPos b) {
        // Diagonal movement costs more
        int dx = Math.abs(a.getX() - b.getX());
        int dz = Math.abs(a.getZ() - b.getZ());
        int dy = Math.abs(a.getY() - b.getY());
        
        if (dx + dz == 2) return 1.414; // Diagonal
        if (dy > 0) return 1.2; // Vertical movement is slightly more expensive
        return 1.0;
    }
    
    private static List<BlockPos> reconstructPath(Node node) {
        List<BlockPos> path = new ArrayList<>();
        Node current = node;
        while (current != null) {
            path.add(current.pos);
            current = current.parent;
        }
        Collections.reverse(path);
        
        if (path.size() > MAX_PATH_LENGTH) {
            return path.subList(0, MAX_PATH_LENGTH);
        }
        
        return path;
    }
    
    private static class Node {
        BlockPos pos;
        Node parent;
        double gScore;
        double fScore;
        
        Node(BlockPos pos, Node parent, double gScore, double fScore) {
            this.pos = pos;
            this.parent = parent;
            this.gScore = gScore;
            this.fScore = fScore;
        }
    }
}
